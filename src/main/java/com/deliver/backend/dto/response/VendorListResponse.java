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
public class VendorListResponse {
    
    private List<VendorSummaryResponse> vendors;
    private Integer totalCount;
    private Boolean hasMore;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VendorSummaryResponse {
        
        private Long id;
        private String name;
        private String description;
        private String category;
        private String subcategory;
        private String logoUrl;
        private String bannerImageUrl;
        private String address;
        private String phone;
        private String email;
        private String website;
        private Boolean isAcceptingOrders;
        private Boolean isVerified;
        private BigDecimal averageRating;
        private Integer reviewCount;
        private Integer totalOrders;
        private String status;
        
        // Mobile app specific fields
        private List<String> tags;
        private String specialFeature;
        private Boolean isVetServiceAvailable;
        private Boolean is24Hours;
        private Boolean isPrescriptionAvailable;
        private Boolean isInstallmentAvailable;
        private Boolean hasDiscount;
        private String discountText;
        private String installmentText;
        private String warrantyText;
        private String consultationText;
        
        // Service info
        private String serviceType;
        private String serviceCategory;
        
        // Delivery info
        private BigDecimal deliveryFee;
        private Integer deliveryTimeMinutes;
        private String deliveryArea;
        private Boolean isFreeDelivery;
        private BigDecimal minimumOrderAmount;
        
        // Business hours
        private String businessHours;
        private Boolean isOpenNow;
        private String nextOpenTime;
        
        // User favorite status
        private Boolean isFavorite;
    }
}
