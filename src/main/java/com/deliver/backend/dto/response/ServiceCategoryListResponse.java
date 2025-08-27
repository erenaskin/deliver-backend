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
public class ServiceCategoryListResponse {
    
    private List<ServiceCategorySummaryResponse> serviceCategories;
    private Integer totalCount;
    private Boolean hasMore;
    
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
    }
}

