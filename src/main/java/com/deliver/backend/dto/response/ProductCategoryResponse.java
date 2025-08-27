package com.deliver.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryResponse {
    
    private Long id;
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
}

