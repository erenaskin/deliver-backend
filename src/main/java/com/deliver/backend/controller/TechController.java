package com.deliver.backend.controller;

import com.deliver.backend.dto.response.ProductResponse;
import com.deliver.backend.dto.response.VendorResponse;
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
    @Operation(summary = "Get popular products", description = "Retrieve popular products")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Popular products retrieved successfully")
    })
    public ResponseEntity<List<ProductResponse>> getPopularProducts() {
        List<ProductResponse> products = techService.getPopularProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/stores")
    @Operation(summary = "Get stores", description = "Retrieve available stores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stores retrieved successfully")
    })
    public ResponseEntity<List<VendorResponse>> getStores() {
        List<VendorResponse> stores = techService.getStores();
        return ResponseEntity.ok(stores);
    }
}