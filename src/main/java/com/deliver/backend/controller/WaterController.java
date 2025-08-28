package com.deliver.backend.controller;

import com.deliver.backend.dto.response.FoodProductResponse;
import com.deliver.backend.dto.response.FoodStoreResponse;
import com.deliver.backend.service.WaterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services/water")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Water", description = "Water-related endpoints")
public class WaterController {

    private final WaterService waterService;

    @GetMapping("/popular-products")
    @Operation(summary = "Get popular water products", description = "Retrieve popular water products")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Popular water products retrieved successfully")
    })
    public ResponseEntity<List<FoodProductResponse>> getPopularProducts() {
        List<FoodProductResponse> products = waterService.getPopularProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/stores")
    @Operation(summary = "Get water stores", description = "Retrieve available water stores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Water stores retrieved successfully")
    })
    public ResponseEntity<List<FoodStoreResponse>> getStores() {
        List<FoodStoreResponse> stores = waterService.getStores();
        return ResponseEntity.ok(stores);
    }
}