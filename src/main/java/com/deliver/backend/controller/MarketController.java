package com.deliver.backend.controller;

import com.deliver.backend.dto.response.FoodProductResponse;
import com.deliver.backend.dto.response.FoodStoreResponse;
import com.deliver.backend.service.MarketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services/market")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Market", description = "Market-related endpoints")
public class MarketController {

    private final MarketService marketService;

    @GetMapping("/popular-products")
    @Operation(summary = "Get popular market products", description = "Retrieve popular market products")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Popular market products retrieved successfully")
    })
    public ResponseEntity<List<FoodProductResponse>> getPopularProducts() {
        List<FoodProductResponse> products = marketService.getPopularProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/stores")
    @Operation(summary = "Get market stores", description = "Retrieve available market stores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Market stores retrieved successfully")
    })
    public ResponseEntity<List<FoodStoreResponse>> getStores() {
        List<FoodStoreResponse> stores = marketService.getStores();
        return ResponseEntity.ok(stores);
    }
}