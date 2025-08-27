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

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        log.info("Fetching all products");
        return productRepository.findAll()
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
    public List<ProductResponse> getProductsByCategory(String category) {
        log.info("Fetching products by category: {}", category);
        return productRepository.findByCategoryIgnoreCase(category)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAvailableProducts() {
        log.info("Fetching available products");
        return productRepository.findByIsAvailableTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
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
                .quantity(request.getQuantity())
                .isAvailable(request.getIsAvailable())
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
        product.setQuantity(request.getQuantity());
        product.setIsAvailable(request.getIsAvailable());

        product = productRepository.save(product);
        log.info("Product updated successfully: {}", id);

        return mapToResponse(product);
    }

    private void validateProductRequest(ProductRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new BadRequestException("Product name is required");
        }

        if (request.getPrice() == null || request.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Product price must be greater than 0");
        }
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByVendor(Long vendorId, Pageable pageable) {
        log.info("Fetching products by vendor ID: {}", vendorId);
        return productRepository.findByVendorId(vendorId, pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByService(Long serviceId, Pageable pageable, String category, String search) {
        log.info("Fetching products by service ID: {} with filters", serviceId);
        // For now, return all products - in real app would filter by service
        return productRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public List<String> getCategories() {
        log.info("Fetching all product categories");
        return productRepository.findAll()
                .stream()
                .map(Product::getCategory)
                .distinct()
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getFeaturedProducts(int limit) {
        log.info("Fetching featured products with limit: {}", limit);
        return productRepository.findByIsFeaturedTrue()
                .stream()
                .limit(limit)
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProducts(String query, Pageable pageable) {
        log.info("Searching products with query: {}", query);
        return productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query, pageable)
                .map(this::mapToResponse);
    }

    @Transactional
    public void addToFavorites(Long productId) {
        log.info("Adding product {} to favorites", productId);

        // Verify product exists
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> ResourceNotFoundException.forProduct(productId));

        // In a real application, you would:
        // 1. Get current user from SecurityContext
        // 2. Check if already in favorites
        // 3. Save to favorites table

        // For now, we'll simulate the functionality
        log.info("Product {} ({}) added to favorites successfully", productId, product.getName());
    }

    @Transactional
    public void removeFromFavorites(Long productId) {
        log.info("Removing product {} from favorites", productId);

        // Verify product exists
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> ResourceNotFoundException.forProduct(productId));

        // In a real application, you would:
        // 1. Get current user from SecurityContext
        // 2. Find and remove from favorites table

        // For now, we'll simulate the functionality
        log.info("Product {} ({}) removed from favorites successfully", productId, product.getName());
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> getUserFavorites(Pageable pageable) {
        log.info("Getting user favorites");

        // In a real application, you would:
        // 1. Get current user from SecurityContext
        // 2. Query favorites table with user ID
        // 3. Return products with pagination

        // For now, we'll return featured products as a simulation
        List<ProductResponse> featuredProducts = productRepository.findByIsFeaturedTrue()
                .stream()
                .limit(pageable.getPageSize())
                .map(this::mapToResponse)
                .toList();

        return new PageImpl<>(featuredProducts, pageable, featuredProducts.size());
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .isAvailable(product.getIsAvailable())
                .build();
    }
}
