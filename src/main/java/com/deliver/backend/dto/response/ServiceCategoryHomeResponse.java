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
public class ServiceCategoryHomeResponse {
    
    private List<ServiceCategorySummaryResponse> serviceCategories;
    
    private List<QuickCategoryResponse> globalQuickCategories;
    
    private List<PopularProductResponse> globalPopularProducts;
    
    private List<VendorListResponse.VendorSummaryResponse> featuredVendors;
    
    private Integer totalServiceCategories;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ServiceCategorySummaryResponse {
        
        private Long id;
        private String name;
        private String displayName;
        private String subtitle;
        private String iconName;
        private String imageUrl;
        private String color;
        private String accentColor;
        private Boolean isActive;
        private Integer displayOrder;
        private Boolean isEmergencyService;
        private String emergencyText;
        private String emergencyButtonText;
    }
}
