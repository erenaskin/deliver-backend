package com.deliver.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteRequest {
    
    private String favoriteType; // "VENDOR", "PRODUCT", "SERVICE_CATEGORY"
    
    private Long vendorId;
    
    private Long productId;
    
    private Long serviceCategoryId;
    
    private Boolean isFavorite; // true to add, false to remove
}

