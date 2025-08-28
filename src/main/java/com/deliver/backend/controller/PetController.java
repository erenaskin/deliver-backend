package com.deliver.backend.controller;

import com.deliver.backend.dto.response.FoodProductResponse;
import com.deliver.backend.dto.response.FoodStoreResponse;
import com.deliver.backend.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services/pet")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Pet", description = "Pet-related endpoints")
public class PetController {

    private final PetService petService;

    @GetMapping("/popular-products")
    @Operation(summary = "Get popular pet products", description = "Retrieve popular pet products")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Popular pet products retrieved successfully")
    })
    public ResponseEntity<List<FoodProductResponse>> getPopularProducts() {
        List<FoodProductResponse> products = petService.getPopularProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/stores")
    @Operation(summary = "Get pet stores", description = "Retrieve available pet stores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pet stores retrieved successfully")
    })
    public ResponseEntity<List<FoodStoreResponse>> getStores() {
        List<FoodStoreResponse> stores = petService.getStores();
        return ResponseEntity.ok(stores);
    }
}