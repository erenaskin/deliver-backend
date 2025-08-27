package com.deliver.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopularProductRequest {
    
    private String name;
    
    private String description;
    
    private String imageUrl;
    
    private BigDecimal price;
    
    private String brand;
    
    private String category;
    
    private String subcategory;
    
    private Boolean isActive;
    
    private Integer displayOrder;
    
    private String productUrl;
    
    private String availabilityStatus;
    
    private Long serviceCategoryId;
}

