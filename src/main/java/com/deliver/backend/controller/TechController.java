package com.deliver.backend.controller;

import com.deliver.backend.dto.response.TechProductResponse;
import com.deliver.backend.dto.response.TechStoreResponse;
import com.deliver.backend.service.TechService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services/tech")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Tech", description = "Tech-related endpoints")
public class TechController {

    private final TechService techService;

    @GetMapping("/popular-products")
    @Operation(summary = "Get popular tech products", description = "Retrieve popular technology products")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Popular tech products retrieved successfully")
    })
    public ResponseEntity<List<TechProductResponse>> getPopularProducts() {
        List<TechProductResponse> products = techService.getPopularProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/stores")
    @Operation(summary = "Get tech stores", description = "Retrieve available technology stores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tech stores retrieved successfully")
    })
    public ResponseEntity<List<TechStoreResponse>> getStores() {
        List<TechStoreResponse> stores = techService.getStores();
        return ResponseEntity.ok(stores);
    }
}