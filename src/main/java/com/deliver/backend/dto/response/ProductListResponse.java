package com.deliver.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductListResponse {
    
    private List<ProductSummaryResponse> products;
    private Integer totalCount;
    private Boolean hasMore;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductSummaryResponse {
        
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private BigDecimal originalPrice;
        private String category;
        private String subcategory;
        private String mainImageUrl;
        private Boolean isAvailable;
        private Boolean isFeatured;
        private BigDecimal averageRating;
        private Integer reviewCount;
        private Integer totalSales;
        private String status;
        
        // Mobile app specific fields
        private String volume;
        private String deliverySpeedText;
        private Boolean isAvailableToday;
        private Boolean isPopular;
        private Boolean isNewArrival;
        private Boolean isPromotion;
        private Boolean isLimitedStock;
        private String brand;
        private BigDecimal deliveryFee;
        private Integer deliveryTimeMinutes;
        
        // Vendor info
        private String vendorName;
        private String vendorLogoUrl;
        private BigDecimal vendorRating;
        private Integer vendorReviewCount;
        private Integer vendorDeliveryTime;
        private BigDecimal vendorDeliveryFee;
        
        // Pet type info for pet products
        private String petTypeName;
        private String petTypeEmoji;
        
        // User favorite status
        private Boolean isFavorite;
    }
}

