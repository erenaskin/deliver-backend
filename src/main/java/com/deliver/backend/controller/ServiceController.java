package com.deliver.backend.controller;

import com.deliver.backend.dto.response.ServiceCategoryResponse;
import com.deliver.backend.dto.response.ServiceCategoryListResponse;
import com.deliver.backend.service.ServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
@Tag(name = "Services", description = "Service management endpoints")
public class ServiceController {

    private final ServiceService serviceService;

    @GetMapping("/categories")
    @Operation(summary = "Get all service categories", description = "Retrieve all available service categories for mobile app")
    public ResponseEntity<ServiceCategoryListResponse> getAllServiceCategories() {
        ServiceCategoryListResponse response = serviceService.getAllServiceCategories();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/categories/{categoryName}")
    @Operation(summary = "Get service category details", description = "Get detailed information about a specific service category")
    public ResponseEntity<ServiceCategoryResponse> getServiceCategoryDetails(
            @PathVariable String categoryName,
            @RequestParam(required = false) String productCategory,
            @RequestParam(required = false) String petType,
            @RequestParam(required = false) String waterType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        ServiceCategoryResponse response = serviceService.getServiceCategoryDetails(
            categoryName, productCategory, petType, waterType, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Search across services", description = "Search for products, vendors, and categories across all services")
    public ResponseEntity<ServiceCategoryResponse> searchServices(
            @RequestParam String query,
            @RequestParam(required = false) String serviceCategory,
            @RequestParam(required = false) String productCategory,
            @RequestParam(required = false) String petType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        ServiceCategoryResponse response = serviceService.searchServices(
            query, serviceCategory, productCategory, petType, page, size);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<ServiceCategoryListResponse>> getServicesRoot() {
        ServiceCategoryListResponse response = serviceService.getAllServiceCategories();
        return ResponseEntity.ok(List.of(response));
    }
}
