package com.deliver.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryRequest {
    
    private String name;
    
    private String displayName;
    
    private String description;
    
    private String iconName;
    
    private String color;
    
    private String backgroundColor;
    
    private String textColor;
    
    private Boolean isActive;
    
    private Boolean isDefault;
    
    private Integer displayOrder;
    
    private String searchKeyword;
    
    private Long serviceCategoryId;
}

