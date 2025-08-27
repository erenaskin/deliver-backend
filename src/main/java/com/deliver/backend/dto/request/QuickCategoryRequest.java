package com.deliver.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuickCategoryRequest {
    
    private String name;
    
    private String emoji;
    
    private String iconName;
    
    private String backgroundColor;
    
    private String textColor;
    
    private Boolean isActive;
    
    private Integer displayOrder;
    
    private String searchKeyword;
    
    private String description;
    
    private Long serviceCategoryId;
}

