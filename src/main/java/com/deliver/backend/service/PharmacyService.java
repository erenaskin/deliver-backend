package com.deliver.backend.service;

import com.deliver.backend.dto.response.FoodProductResponse;
import com.deliver.backend.dto.response.FoodStoreResponse;
import com.deliver.backend.entity.Product;
import com.deliver.backend.entity.ServiceType;
import com.deliver.backend.entity.Vendor;
import com.deliver.backend.repository.ProductRepository;
import com.deliver.backend.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class PharmacyService {

    private final ProductRepository productRepository;
    private final VendorRepository vendorRepository;
    private final Random random = new Random();

    @Transactional(readOnly = true)
    public List<FoodProductResponse> getPopularProducts() {
        log.info("Fetching popular pharmacy products");

        List<Product> featuredPharmacyProducts = productRepository.findByServiceTypeAndIsFeaturedTrue(ServiceType.PHARMACY.name());

        return featuredPharmacyProducts.stream()
                .limit(10)
                .map(this::mapToFoodProductResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FoodStoreResponse> getStores() {
        log.info("Fetching pharmacy stores");

        List<Vendor> activePharmacyVendors = vendorRepository.findByServiceTypeAndStatus(ServiceType.PHARMACY.name(), Vendor.VendorStatus.ACTIVE);

        return activePharmacyVendors.stream()
                .limit(20)
                .map(this::mapToFoodStoreResponse)
                .toList();
    }

    private FoodProductResponse mapToFoodProductResponse(Product product) {
        return FoodProductResponse.builder()
                .name(product.getName())
                .category(product.getCategory() != null ? product.getCategory() : "İlaç & Sağlık")
                .price(product.getPrice())
                .emoji(getProductEmoji(product.getCategory()))
                .build();
    }

    private FoodStoreResponse mapToFoodStoreResponse(Vendor vendor) {
        return FoodStoreResponse.builder()
                .name(vendor.getBusinessName())
                .description(vendor.getDescription() != null ? vendor.getDescription() : "İlaç ve sağlık ürünleri")
                .rating(vendor.getAverageRating())
                .deliveryTime(generateDeliveryTime())
                .deliveryFee(vendor.getDeliveryFee() != null ? vendor.getDeliveryFee() : BigDecimal.valueOf(4.99))
                .image(getStoreEmoji(vendor.getCategory()))
                .hasDiscount(random.nextBoolean())
                .cuisineType(vendor.getCategory())
                .build();
    }

    private String getProductEmoji(String category) {
        if (category == null) return "💊";

        String lowerCategory = category.toLowerCase();

        if (lowerCategory.contains("ilaç") || lowerCategory.contains("medicine")) {
            return "💊";
        } else if (lowerCategory.contains("vitamin")) {
            return "🧴";
        } else if (lowerCategory.contains("takviye")) {
            return "💊";
        } else if (lowerCategory.contains("malzeme") || lowerCategory.contains("supplies")) {
            return "🩺";
        } else {
            return "💊";
        }
    }

    private String getStoreEmoji(String category) {
        if (category == null) return "🏪";

        String lowerCategory = category.toLowerCase();

        if (lowerCategory.contains("eczane") || lowerCategory.contains("pharmacy")) {
            return "💊";
        } else {
            return "🏪";
        }
    }

    private String generateDeliveryTime() {
        int min = 25 + random.nextInt(30);
        int max = min + 15 + random.nextInt(25);
        return min + "-" + max + " dk";
    }
}