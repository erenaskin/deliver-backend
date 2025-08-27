package com.deliver.backend.controller;

import com.deliver.backend.dto.response.ProductResponse;
import com.deliver.backend.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Products", description = "Product management endpoints")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieve paginated list of products")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @Parameter(description = "Pagination parameters") Pageable pageable,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        
        Page<ProductResponse> products = productService.getAllProducts(pageable, category, search, minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieve a specific product by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product found"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(product);
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
