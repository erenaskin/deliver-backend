package com.deliver.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCategoryResponse {
    
    private Long id;
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
    
    // Products and vendors for this service category
    private List<ProductListResponse.ProductSummaryResponse> products;
    private List<VendorListResponse.VendorSummaryResponse> vendors;
    
    // Emergency service data if applicable
    private EmergencyServiceResponse emergencyService;
    
    // Quick categories for the service
    private List<QuickCategoryResponse> quickCategories;
    
    // Product categories for filtering
    private List<ProductCategoryResponse> productCategories;
    
    // Popular products if applicable (e.g., for technology service)
    private List<PopularProductResponse> popularProducts;
}
