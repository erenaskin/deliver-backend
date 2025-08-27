package com.deliver.backend.dto.request;

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
public class SearchRequest {
    
    private String query; // Search term
    
    private String serviceCategory; // e.g., "Eczane", "Pet", "Food", "Su", "Market", "Teknoloji"
    
    private String productCategory; // e.g., "Tümü", "Ağrı Kesici", "Soğuk Algınlığı"
    
    private String petType; // e.g., "Kedi", "Köpek", "Kuş", "Balık"
    
    private List<String> tags; // e.g., ["VET", "BAKIM", "TAKSİT"]
    
    private BigDecimal minPrice;
    
    private BigDecimal maxPrice;
    
    private Integer maxDeliveryTime; // Maximum delivery time in minutes
    
    private BigDecimal maxDeliveryFee;
    
    private Boolean isAvailableToday; // For water delivery
    
    private Boolean isPopular; // For popular products
    
    private Boolean isNewArrival; // For new products
    
    private Boolean isPromotion; // For promotional products
    
    private Boolean isLimitedStock; // For limited stock products
    
    private String sortBy; // "price", "rating", "deliveryTime", "name"
    
    private String sortOrder; // "asc", "desc"
    
    private Integer page;
    
    private Integer size;
    
    // Location-based search
    private BigDecimal latitude;
    
    private BigDecimal longitude;
    
    private BigDecimal radiusKm; // Search radius in kilometers
}

