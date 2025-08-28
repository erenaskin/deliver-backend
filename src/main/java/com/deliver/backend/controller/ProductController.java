package com.deliver.backend.controller;

import com.deliver.backend.dto.request.ProductRequest;
import com.deliver.backend.dto.response.ProductResponse;
import com.deliver.backend.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product management endpoints")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieve all products")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieve a specific product by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product found"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get products by category", description = "Retrieve products by category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable String category) {
        List<ProductResponse> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/available")
    @Operation(summary = "Get available products", description = "Retrieve available products")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    public ResponseEntity<List<ProductResponse>> getAvailableProducts() {
        List<ProductResponse> products = productService.getAvailableProducts();
        return ResponseEntity.ok(products);
    }

    @PostMapping
    @Operation(summary = "Create new product", description = "Create a new product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid product data")
    })
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product", description = "Update an existing product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "400", description = "Invalid product data")
    })
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        ProductResponse response = productService.updateProduct(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vendor/{vendorId}")
    @Operation(summary = "Get products by vendor", description = "Retrieve all products from a specific vendor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Vendor not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Page<ProductResponse>> getProductsByVendor(
            @PathVariable Long vendorId,
            @Parameter(description = "Pagination parameters") Pageable pageable) {

        Page<ProductResponse> products = productService.getProductsByVendor(vendorId, pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/service/{serviceId}")
    @Operation(summary = "Get products by service", description = "Retrieve all products from a specific service")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Service not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Page<ProductResponse>> getProductsByService(
            @PathVariable Long serviceId,
            @Parameter(description = "Pagination parameters") Pageable pageable,
            @Parameter(description = "Category filter") @RequestParam(required = false) String category,
            @Parameter(description = "Search query") @RequestParam(required = false) String search) {

        Page<ProductResponse> products = productService.getProductsByService(serviceId, pageable, category, search);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/categories")
    @Operation(summary = "Get product categories", description = "Retrieve all available product categories")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = productService.getCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/featured")
    @Operation(summary = "Get featured products", description = "Retrieve featured/popular products")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Featured products retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<ProductResponse>> getFeaturedProducts(@RequestParam(defaultValue = "10") int limit) {
        List<ProductResponse> products = productService.getFeaturedProducts(limit);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/popular/{serviceType}")
    @Operation(summary = "Get popular products by service type", description = "Retrieve popular products based on popularity score")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Popular products retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<ProductResponse>> getPopularProductsByServiceType(
            @PathVariable String serviceType,
            @RequestParam(defaultValue = "10") int limit) {
        List<ProductResponse> products = productService.getPopularProductsByServiceType(serviceType, limit);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/daily-popular/{serviceType}")
    @Operation(summary = "Get daily popular products", description = "Retrieve products popular in the last 24 hours")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Daily popular products retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<ProductResponse>> getDailyPopularProducts(
            @PathVariable String serviceType,
            @RequestParam(defaultValue = "10") int limit) {
        List<ProductResponse> products = productService.getDailyPopularProducts(serviceType, limit);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/top-rated/{serviceType}")
    @Operation(summary = "Get top rated products", description = "Retrieve highest rated products with minimum 5 ratings")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Top rated products retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<ProductResponse>> getTopRatedProducts(
            @PathVariable String serviceType,
            @RequestParam(defaultValue = "10") int limit) {
        List<ProductResponse> products = productService.getTopRatedProducts(serviceType, limit);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/best-selling/{serviceType}")
    @Operation(summary = "Get best selling products", description = "Retrieve products with highest order count")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Best selling products retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<ProductResponse>> getBestSellingProducts(
            @PathVariable String serviceType,
            @RequestParam(defaultValue = "10") int limit) {
        List<ProductResponse> products = productService.getBestSellingProducts(serviceType, limit);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/trending/{serviceType}")
    @Operation(summary = "Get trending products", description = "Retrieve trending products from the last week")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trending products retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<ProductResponse>> getTrendingProducts(
            @PathVariable String serviceType,
            @RequestParam(defaultValue = "10") int limit) {
        List<ProductResponse> products = productService.getTrendingProducts(serviceType, limit);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    @Operation(summary = "Search products", description = "Search products by name, description, or category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Page<ProductResponse>> searchProducts(
            @RequestParam String query,
            @Parameter(description = "Pagination parameters") Pageable pageable) {

        Page<ProductResponse> products = productService.searchProducts(query, pageable);
        return ResponseEntity.ok(products);
    }

    @PostMapping("/{id}/favorite")
    @Operation(summary = "Add to favorites", description = "Add product to user's favorites")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product added to favorites"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> addToFavorites(@PathVariable Long id) {
        productService.addToFavorites(id);
        return ResponseEntity.ok("Product added to favorites");
    }

    @DeleteMapping("/{id}/favorite")
    @Operation(summary = "Remove from favorites", description = "Remove product from user's favorites")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product removed from favorites"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> removeFromFavorites(@PathVariable Long id) {
        productService.removeFromFavorites(id);
        return ResponseEntity.ok("Product removed from favorites");
    }

    @GetMapping("/favorites")
    @Operation(summary = "Get user favorites", description = "Retrieve user's favorite products")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Favorites retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<ProductResponse>> getFavorites(
            @Parameter(description = "Pagination parameters") Pageable pageable) {

        Page<ProductResponse> favorites = productService.getUserFavorites(pageable);
        return ResponseEntity.ok(favorites);
    }
}
