package com.deliver.backend.controller;

import com.deliver.backend.dto.response.FoodProductResponse;
import com.deliver.backend.dto.response.FoodStoreResponse;
import com.deliver.backend.service.PharmacyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services/pharmacy")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Pharmacy", description = "Pharmacy-related endpoints")
public class PharmacyController {

    private final PharmacyService pharmacyService;

    @GetMapping("/popular-products")
    @Operation(summary = "Get popular pharmacy products", description = "Retrieve popular pharmacy products")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Popular pharmacy products retrieved successfully")
    })
    public ResponseEntity<List<FoodProductResponse>> getPopularProducts() {
        List<FoodProductResponse> products = pharmacyService.getPopularProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/stores")
    @Operation(summary = "Get pharmacy stores", description = "Retrieve available pharmacy stores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pharmacy stores retrieved successfully")
    })
    public ResponseEntity<List<FoodStoreResponse>> getStores() {
        List<FoodStoreResponse> stores = pharmacyService.getStores();
        return ResponseEntity.ok(stores);
    }
}