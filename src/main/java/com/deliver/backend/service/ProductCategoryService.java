package com.deliver.backend.service;

import com.deliver.backend.entity.ProductCategory;
import com.deliver.backend.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {
    
    private final ProductCategoryRepository productCategoryRepository;
    
    public List<ProductCategory> getCategoriesByService(Long serviceId) {
        return productCategoryRepository.findByServiceCategory_IdAndIsActiveTrue(serviceId);
    }
    
    public List<ProductCategory> getCategoriesByServiceOrdered(Long serviceId) {
        return productCategoryRepository.findActiveCategoriesByServiceOrdered(serviceId);
    }
    
    public ProductCategory getCategoryById(Long id) {
        return productCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product category not found with id: " + id));
    }
    
    public List<ProductCategory> searchCategories(String name, Long serviceId) {
        // For now, just search by name without service filtering
        return productCategoryRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(name);
    }
}
