package com.deliver.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCategoryHomeRequest {
    
    private Boolean includeInactive;
    
    private Integer maxServiceCategories;
    
    private Integer maxQuickCategories;
    
    private Integer maxPopularProducts;
    
    private Integer maxFeaturedVendors;
    
    // Location-based filtering
    private Double latitude;
    
    private Double longitude;
    
    private Double radiusKm; // Search radius in kilometers
    
    // User preferences
    private String userPreferredServiceCategory;
    
    private Boolean includeEmergencyServices;
}
