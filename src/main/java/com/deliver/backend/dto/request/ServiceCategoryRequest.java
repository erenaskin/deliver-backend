package com.deliver.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCategoryRequest {
    
    private String name;
    
    private String displayName;
    
    private String description;
    
    private String subtitle;
    
    private String iconName;
    
    private String imageUrl;
    
    private String bannerImageUrl;
    
    private String color;
    
    private String accentColor;
    
    private Boolean isActive;
    
    private Integer displayOrder;
    
    private Boolean isEmergencyService;
    
    private String emergencyText;
    
    private String emergencyButtonText;
}

