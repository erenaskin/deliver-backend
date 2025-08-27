package com.deliver.backend.service;

import com.deliver.backend.dto.request.ProductRequest;
import com.deliver.backend.dto.response.ProductResponse;
import com.deliver.backend.entity.Product;
import com.deliver.backend.exception.BadRequestException;
import com.deliver.backend.exception.ResourceNotFoundException;
import com.deliver.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    // ------------------- BASIC PRODUCT METHODS -------------------
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        log.info("Fetching all active products");
        return productRepository.findByIsAvailableTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        log.info("Fetching product with ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forProduct(id));
        return mapToResponse(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByVendor(Long vendorId) {
        log.info("Fetching products for vendor ID: {}", vendorId);
        return productRepository.findByVendorId(vendorId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByCategory(String category) {
        log.info("Fetching products by category: {}", category);
        return productRepository.findByCategoryIgnoreCase(category)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProducts(String keyword, Pageable pageable) {
        log.info("Searching products with keyword: {}", keyword);
        return productRepository.searchProducts(keyword, null, null, null, pageable)
                .map(this::mapToResponse);
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        log.info("Creating new product: {}", request.getName());

        validateProductRequest(request);

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .price(request.getPrice())
                .originalPrice(request.getOriginalPrice() != null ? request.getOriginalPrice() : request.getPrice())
                .quantity(100) // Default stock
                .minOrderQuantity(1) // Default minimum order
                .isAvailable(true)
                .isFeatured(false)
                .averageRating(BigDecimal.ZERO)
                .totalSales(0)
                .status(Product.ProductStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        product = productRepository.save(product);
        log.info("Product created successfully with ID: {}", product.getId());

        return mapToResponse(product);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        log.info("Updating product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forProduct(id));

        validateProductRequest(request);

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setOriginalPrice(request.getOriginalPrice() != null ? request.getOriginalPrice() : request.getPrice());
        product.setUpdatedAt(LocalDateTime.now());

        product = productRepository.save(product);
        log.info("Product updated successfully: {}", id);

        return mapToResponse(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forProduct(id));

        // Soft delete
        product.setStatus(Product.ProductStatus.INACTIVE);
        product.setIsAvailable(false);
        product.setUpdatedAt(LocalDateTime.now());

        productRepository.save(product);
        log.info("Product marked as inactive: {}", id);
    }

    private void validateProductRequest(ProductRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new BadRequestException("Product name is required");
        }

        if (request.getPrice() == null || request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Product price must be greater than 0");
        }
    }

    private ProductResponse mapToResponse(Product product) {
        ProductResponse.VendorInfo vendorInfo = null;
        if (product.getVendor() != null) {
            vendorInfo = ProductResponse.VendorInfo.builder()
                    .id(product.getVendor().getId())
                    .name(product.getVendor().getBusinessName())
                    .logoUrl(product.getVendor().getLogoUrl())
                    .rating(product.getVendor().getAverageRating().doubleValue())
                    .reviewCount(product.getVendor().getReviewCount())
                    .isOpen(product.getVendor().getIsAcceptingOrders())
                    .build();
        }

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .subcategory(product.getSubcategory())
                .price(product.getPrice())
                .originalPrice(product.getOriginalPrice())
                .discountPercentage(product.getDiscountPercentage())
                .imageUrls(product.getImageUrls())
                .mainImageUrl(product.getMainImageUrl())
                .quantity(product.getQuantity())
                .minOrderQuantity(product.getMinOrderQuantity())
                .maxOrderQuantity(product.getMaxOrderQuantity())
                .isAvailable(product.getIsAvailable())
                .isFeatured(product.getIsFeatured())
                .averageRating(product.getAverageRating().doubleValue())
                .reviewCount(product.getReviewCount())
                .vendor(vendorInfo)
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    // ------------------- CONTROLLER UYUMLU METHODS -------------------
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(Pageable pageable, String category, String search, BigDecimal minPrice, BigDecimal maxPrice) {
        log.info("Fetching products with filters - category: {}, search: {}, minPrice: {}, maxPrice: {}", category, search, minPrice, maxPrice);
        return productRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByVendor(Long vendorId, Pageable pageable) {
        List<Product> products = productRepository.findByVendorId(vendorId);
        return new PageImpl<>(products.stream().map(this::mapToResponse).toList(), pageable, products.size());
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByService(Long serviceId, Pageable pageable, String category, String search) {
        Page<Product> productsPage = productRepository.findByServiceId(serviceId, pageable);
        List<Product> products = productsPage.getContent();

        if (category != null && !category.trim().isEmpty()) {
            products = products.stream()
                    .filter(p -> category.equalsIgnoreCase(p.getCategory()))
                    .toList();
        }

        if (search != null && !search.trim().isEmpty()) {
            String lowerSearch = search.toLowerCase();
            products = products.stream()
                    .filter(p -> p.getName().toLowerCase().contains(lowerSearch) ||
                            p.getDescription().toLowerCase().contains(lowerSearch))
                    .toList();
        }

        return new PageImpl<>(products.stream().map(this::mapToResponse).toList(), pageable, products.size());
    }

    @Transactional(readOnly = true)
    public List<String> getCategories() {
        log.info("Fetching all product categories");
        return List.of("Pizza", "Burger", "Pasta", "Salad", "Dessert", "Beverage");
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getFeaturedProducts(int limit) {
        return productRepository.findByIsFeaturedTrue().stream()
                .limit(limit)
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public void addToFavorites(Long productId) {
        log.info("Adding product {} to favorites", productId);
    }

    @Transactional
    public void removeFromFavorites(Long productId) {
        log.info("Removing product {} from favorites", productId);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> getUserFavorites(Pageable pageable) {
        log.info("Fetching user favorites");
        return Page.empty(pageable);
    }
}
