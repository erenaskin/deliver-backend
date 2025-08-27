package com.deliver.backend.controller;

import com.deliver.backend.dto.response.FoodProductResponse;
import com.deliver.backend.dto.response.FoodStoreResponse;
import com.deliver.backend.service.FoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services/food")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Food", description = "Food-related endpoints")
public class FoodController {

    private final FoodService foodService;

    @GetMapping("/popular-products")
    @Operation(summary = "Get popular food products", description = "Retrieve popular food products")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Popular food products retrieved successfully")
    })
    public ResponseEntity<List<FoodProductResponse>> getPopularProducts() {
        List<FoodProductResponse> products = foodService.getPopularProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/stores")
    @Operation(summary = "Get food stores", description = "Retrieve available food stores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Food stores retrieved successfully")
    })
    public ResponseEntity<List<FoodStoreResponse>> getStores() {
        List<FoodStoreResponse> stores = foodService.getStores();
        return ResponseEntity.ok(stores);
    }
}