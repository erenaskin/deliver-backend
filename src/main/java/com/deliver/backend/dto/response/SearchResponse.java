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
public class SearchResponse {
    
    private String query;
    
    private String serviceCategory;
    
    private List<ProductListResponse.ProductSummaryResponse> products;
    
    private List<VendorListResponse.VendorSummaryResponse> vendors;
    
    private List<QuickCategoryResponse> quickCategories;
    
    private List<ProductCategoryResponse> productCategories;
    
    private List<PopularProductResponse> popularProducts;
    
    private EmergencyServiceResponse emergencyService;
    
    private Integer totalResults;
    
    private Integer page;
    
    private Integer size;
    
    private Boolean hasMore;
}
