package com.deliver.backend.controller;

import com.deliver.backend.dto.request.VendorRequest;
import com.deliver.backend.dto.response.VendorResponse;
import com.deliver.backend.service.VendorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendors")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Vendors", description = "Vendor management endpoints")
public class VendorController {

    private final VendorService vendorService;

    @GetMapping
    @Operation(summary = "Get all vendors", description = "Retrieve paginated list of all vendors")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vendors retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Page<VendorResponse>> getAllVendors(
            @Parameter(description = "Pagination parameters") Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean isActive) {
        
        Page<VendorResponse> vendors = vendorService.getAllVendors(pageable, search, category, isActive);
        return ResponseEntity.ok(vendors);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get vendor by ID", description = "Retrieve specific vendor details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vendor found"),
        @ApiResponse(responseCode = "404", description = "Vendor not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<VendorResponse> getVendorById(@PathVariable Long id) {
        VendorResponse vendor = vendorService.getVendorById(id);
        return ResponseEntity.ok(vendor);
    }

    @PostMapping("/register")
    @Operation(summary = "Register as vendor", description = "Register current user as a vendor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Vendor registration successful"),
        @ApiResponse(responseCode = "400", description = "Invalid vendor data"),
        @ApiResponse(responseCode = "409", description = "User already registered as vendor"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> registerVendor(@Valid @RequestBody VendorRequest vendorRequest) {
        vendorService.registerVendor(vendorRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Vendor registration submitted for approval");
    }

    @GetMapping("/profile")
    @Operation(summary = "Get vendor profile", description = "Get current vendor's profile information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Vendor profile not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<VendorResponse> getVendorProfile() {
        VendorResponse profile = vendorService.getVendorProfile();
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/profile")
    @Operation(summary = "Update vendor profile", description = "Update current vendor's profile information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid profile data"),
        @ApiResponse(responseCode = "404", description = "Vendor not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<String> updateVendorProfile(@Valid @RequestBody VendorRequest updateRequest) {
        vendorService.updateVendorProfile(updateRequest);
        return ResponseEntity.ok("Vendor profile updated successfully");
    }

    @PostMapping("/products")
    @Operation(summary = "Create product", description = "Create a new product (Vendor only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid product data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<String> createProduct(@Valid @RequestBody Object productRequest) {
        Long productId = vendorService.createProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Product created successfully with ID: " + productId);
    }

    @PutMapping("/products/{productId}")
    @Operation(summary = "Update product", description = "Update existing product (Vendor only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid product data"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<String> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody Object updateRequest) {
        
        vendorService.updateProduct(productId, updateRequest);
        return ResponseEntity.ok("Product updated successfully");
    }

    @DeleteMapping("/products/{productId}")
    @Operation(summary = "Delete product", description = "Delete existing product (Vendor only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        vendorService.deleteProduct(productId);
        return ResponseEntity.ok("Product deleted successfully");
    }

    @GetMapping("/products")
    @Operation(summary = "Get vendor products", description = "Get all products for current vendor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<Page<Object>> getVendorProducts(
            @Parameter(description = "Pagination parameters") Pageable pageable,
            @RequestParam(required = false) String status) {
        
        Page<Object> products = vendorService.getVendorProducts(pageable, status);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/analytics")
    @Operation(summary = "Get vendor analytics", description = "Get analytics and statistics for vendor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Analytics retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<Object> getVendorAnalytics(
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String type) {
        
        Object analytics = vendorService.getVendorAnalytics(period, type);
        return ResponseEntity.ok(analytics);
    }

    @GetMapping("/{id}/reviews")
    @Operation(summary = "Get vendor reviews", description = "Get reviews and ratings for specific vendor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Vendor not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Page<Object>> getVendorReviews(
            @PathVariable Long id,
            @Parameter(description = "Pagination parameters") Pageable pageable) {
        
        Page<Object> reviews = vendorService.getVendorReviews(id, pageable);
        return ResponseEntity.ok(reviews);
    }

    // Admin endpoints for vendor management
    @PutMapping("/{id}/approve")
    @Operation(summary = "Approve vendor", description = "Approve vendor registration (Admin only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vendor approved successfully"),
        @ApiResponse(responseCode = "404", description = "Vendor not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> approveVendor(@PathVariable Long id) {
        vendorService.approveVendor(id);
        return ResponseEntity.ok("Vendor approved successfully");
    }

    @PutMapping("/{id}/reject")
    @Operation(summary = "Reject vendor", description = "Reject vendor registration (Admin only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vendor rejected successfully"),
        @ApiResponse(responseCode = "404", description = "Vendor not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> rejectVendor(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        
        vendorService.rejectVendor(id, reason);
        return ResponseEntity.ok("Vendor registration rejected");
    }
}
