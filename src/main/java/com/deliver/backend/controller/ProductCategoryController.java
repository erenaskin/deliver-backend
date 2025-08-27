package com.deliver.backend.controller;

import com.deliver.backend.entity.ProductCategory;
import com.deliver.backend.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductCategoryController {
    
    private final ProductCategoryService productCategoryService;
    
    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<ProductCategory>> getCategoriesByService(@PathVariable Long serviceId) {
        List<ProductCategory> categories = productCategoryService.getCategoriesByService(serviceId);
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/service/{serviceId}/ordered")
    public ResponseEntity<List<ProductCategory>> getCategoriesByServiceOrdered(@PathVariable Long serviceId) {
        List<ProductCategory> categories = productCategoryService.getCategoriesByServiceOrdered(serviceId);
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProductCategory> getCategoryById(@PathVariable Long id) {
        ProductCategory category = productCategoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<ProductCategory>> searchCategories(
            @RequestParam String name,
            @RequestParam(required = false) Long serviceId) {
        List<ProductCategory> categories = productCategoryService.searchCategories(name, serviceId);
        return ResponseEntity.ok(categories);
    }
}
