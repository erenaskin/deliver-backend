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
public class ServiceCategoryDetailResponse {
    
    private ServiceCategoryResponse serviceCategory;
    
    private EmergencyServiceResponse emergencyService;
    
    private List<QuickCategoryResponse> quickCategories;
    
    private List<ProductCategoryResponse> productCategories;
    
    private List<PopularProductResponse> popularProducts;
    
    private List<ProductListResponse.ProductSummaryResponse> products;
    
    private List<VendorListResponse.VendorSummaryResponse> vendors;
    
    private Integer totalProducts;
    
    private Integer totalVendors;
    
    private Integer page;
    
    private Integer size;
    
    private Boolean hasMore;
    
    // Filtering options
    private List<String> availablePetTypes;
    
    private List<String> availableWaterTypes;
    
    private List<String> availableProductCategories;
    
    private List<String> availableTags;
}

