package com.deliver.backend.service;

import com.deliver.backend.dto.response.ServiceCategoryResponse;
import com.deliver.backend.dto.response.ServiceCategoryListResponse;
import com.deliver.backend.dto.response.ProductListResponse;
import com.deliver.backend.dto.response.VendorListResponse;
import com.deliver.backend.entity.Service;
import com.deliver.backend.entity.Product;
import com.deliver.backend.entity.Vendor;
import com.deliver.backend.repository.ServiceRepository;
import com.deliver.backend.repository.ProductRepository;
import com.deliver.backend.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final ProductRepository productRepository;
    private final VendorRepository vendorRepository;

    public ServiceCategoryListResponse getAllServiceCategories() {
        List<Service> services = serviceRepository.findByIsActiveTrueOrderByOrder();
        
        List<ServiceCategoryListResponse.ServiceCategorySummaryResponse> categories = services.stream()
            .map(this::mapToServiceCategorySummary)
            .collect(Collectors.toList());
        
        return ServiceCategoryListResponse.builder()
            .serviceCategories(categories)
            .totalCount(categories.size())
            .hasMore(false)
            .build();
    }

    public ServiceCategoryResponse getServiceCategoryDetails(String categoryName, String productCategory, 
                                                          String petType, String waterType, int page, int size) {
        Service service = serviceRepository.findByCategoryIgnoreCaseAndIsActiveTrue(categoryName)
            .orElseThrow(() -> new RuntimeException("Service category not found"));
        
        Pageable pageable = PageRequest.of(page, size);
        
        // Get products based on filters
        Page<Product> products = getFilteredProducts(service.getId(), productCategory, petType, waterType, pageable);
        
        // Get vendors
        List<Vendor> vendors = vendorRepository.findByServiceIdAndIsAcceptingOrdersTrue(service.getId());
        
        // Convert products and vendors to response format
        List<ProductListResponse.ProductSummaryResponse> productResponses = products.getContent().stream()
            .map(this::mapToProductSummary)
            .collect(Collectors.toList());
            
        List<VendorListResponse.VendorSummaryResponse> vendorResponses = vendors.stream()
            .map(this::mapToVendorSummary)
            .collect(Collectors.toList());
        
        return ServiceCategoryResponse.builder()
            .id(service.getId())
            .name(service.getName())
            .description(service.getDescription())
            .iconName(service.getIconName())
            .imageUrl(service.getImageUrl())
            .isActive(service.isActive())
            .displayOrder(service.getOrder())
            .products(productResponses)
            .vendors(vendorResponses)
            .build();
    }

    public ServiceCategoryResponse searchServices(String query, String serviceCategory, String productCategory, 
                                               String petType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        
        // Search products
        Page<Product> products = productRepository.searchProducts(query, serviceCategory, productCategory, petType, pageable);
        
        // Search vendors
        Page<Vendor> vendors = vendorRepository.searchVendors(query, serviceCategory, pageable);
        
        // Convert to response format
        List<ProductListResponse.ProductSummaryResponse> productResponses = products.getContent().stream()
            .map(this::mapToProductSummary)
            .collect(Collectors.toList());
            
        List<VendorListResponse.VendorSummaryResponse> vendorResponses = vendors.getContent().stream()
            .map(this::mapToVendorSummary)
            .collect(Collectors.toList());
        
        return ServiceCategoryResponse.builder()
            .id(1L) // Default ID for search results
            .name("Search Results")
            .displayName("Search Results")
            .description("Search results for: " + query)
            .subtitle("Search Results")
            .iconName("search")
            .imageUrl(null)
            .bannerImageUrl(null)
            .color("#000000")
            .accentColor("#FFFFFF")
            .isActive(true)
            .displayOrder(0)
            .isEmergencyService(false)
            .emergencyText(null)
            .emergencyButtonText(null)
            .products(productResponses)
            .vendors(vendorResponses)
            .build();
    }

    private Page<Product> getFilteredProducts(Long serviceId, String productCategory, String petType, 
                                           String waterType, Pageable pageable) {
        if (productCategory != null && !productCategory.equals("Tümü")) {
            return productRepository.findByServiceIdAndCategoryIgnoreCase(serviceId, productCategory, pageable);
        } else if (petType != null) {
            return productRepository.findByServiceIdAndPetTypeIgnoreCase(serviceId, petType, pageable);
        } else if (waterType != null) {
            return productRepository.findByServiceIdAndWaterTypeIgnoreCase(serviceId, waterType, pageable);
        } else {
            return productRepository.findByServiceId(serviceId, pageable);
        }
    }

    private ServiceCategoryListResponse.ServiceCategorySummaryResponse mapToServiceCategorySummary(Service service) {
        return ServiceCategoryListResponse.ServiceCategorySummaryResponse.builder()
            .id(service.getId())
            .name(service.getName())
            .iconName(service.getIconName())
            .imageUrl(service.getImageUrl())
            .isActive(service.isActive())
            .displayOrder(service.getOrder())
            .build();
    }
    
    private ProductListResponse.ProductSummaryResponse mapToProductSummary(Product product) {
        return ProductListResponse.ProductSummaryResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .originalPrice(product.getOriginalPrice())
            .category(product.getCategory())
            .subcategory(product.getSubcategory())
            .mainImageUrl(product.getMainImageUrl())
            .isAvailable(product.getIsAvailable())
            .isFeatured(product.getIsFeatured())
            .averageRating(product.getAverageRating())
            .reviewCount(product.getReviewCount())
            .totalSales(product.getTotalSales())
            .status(product.getStatus().toString())
            .volume(product.getVolume())
            .deliverySpeedText(product.getDeliverySpeedText())
            .isAvailableToday(product.getIsAvailableToday())
            .isPopular(product.getIsPopular())
            .isNewArrival(product.getIsNewArrival())
            .isPromotion(product.getIsPromotion())
            .isLimitedStock(product.getIsLimitedStock())
            .brand(product.getBrand())
            .deliveryFee(product.getDeliveryFee())
            .deliveryTimeMinutes(product.getDeliveryTimeMinutes())
            .vendorName(product.getVendor() != null ? product.getVendor().getBusinessName() : null)
            .vendorLogoUrl(product.getVendor() != null ? product.getVendor().getLogoUrl() : null)
            .vendorRating(product.getVendor() != null ? product.getVendor().getAverageRating() : null)
            .vendorReviewCount(product.getVendor() != null ? product.getVendor().getReviewCount() : null)
            .vendorDeliveryTime(product.getVendor() != null ? product.getVendor().getEstimatedDeliveryTimeMinutes() : null)
            .vendorDeliveryFee(product.getVendor() != null ? product.getVendor().getDeliveryFee() : null)
            .petTypeName(product.getPetType() != null ? product.getPetType().getName() : null)
            .petTypeEmoji(product.getPetType() != null ? product.getPetType().getEmoji() : null)
            .isFavorite(false) // Default value
            .build();
    }
    
    private VendorListResponse.VendorSummaryResponse mapToVendorSummary(Vendor vendor) {
        return VendorListResponse.VendorSummaryResponse.builder()
            .id(vendor.getId())
            .name(vendor.getBusinessName())
            .description(vendor.getDescription())
            .category(vendor.getCategory())
            .subcategory(vendor.getSubcategory())
            .logoUrl(vendor.getLogoUrl())
            .bannerImageUrl(vendor.getBannerImageUrl())
            .address(vendor.getAddress())
            .phone(vendor.getPhoneNumber())
            .email(vendor.getBusinessEmail())
            .website(vendor.getWebsiteUrl())
            .isAcceptingOrders(vendor.getIsAcceptingOrders())
            .isVerified(vendor.getStatus() == Vendor.VendorStatus.ACTIVE)
            .averageRating(vendor.getAverageRating())
            .reviewCount(vendor.getReviewCount())
            .totalOrders(vendor.getTotalOrders())
            .status(vendor.getStatus().toString())
            .tags(vendor.getTags() != null ? new ArrayList<>(vendor.getTags()) : null)
            .specialFeature(vendor.getSpecialFeature())
            .isVetServiceAvailable(vendor.getIsVetServiceAvailable())
            .is24Hours(vendor.getIs24Hours())
            .isPrescriptionAvailable(vendor.getIsPrescriptionAvailable())
            .isInstallmentAvailable(vendor.getIsInstallmentAvailable())
            .hasDiscount(vendor.getHasDiscount())
            .discountText(vendor.getDiscountText())
            .installmentText(vendor.getInstallmentText())
            .warrantyText(vendor.getWarrantyText())
            .consultationText(vendor.getConsultationText())
            .serviceType(vendor.getServiceType())
            .deliveryFee(vendor.getDeliveryFee())
            .deliveryTimeMinutes(vendor.getEstimatedDeliveryTimeMinutes())
            .deliveryArea(vendor.getDeliveryRadiusKm() + " km")
            .isFreeDelivery(vendor.getDeliveryFee().equals(BigDecimal.ZERO))
            .minimumOrderAmount(vendor.getMinimumOrderAmount())
            .isFavorite(false) // Default value
            .build();
    }
}
