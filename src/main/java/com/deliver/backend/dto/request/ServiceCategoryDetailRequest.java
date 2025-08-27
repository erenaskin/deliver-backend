package com.deliver.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCategoryDetailRequest {
    
    private String serviceCategoryName; // e.g., "Eczane", "Pet", "Food", "Su", "Market", "Teknoloji"
    
    private String productCategory; // e.g., "Tümü", "Ağrı Kesici", "Soğuk Algınlığı"
    
    private String petType; // e.g., "Kedi", "Köpek", "Kuş", "Balık"
    
    private String waterType; // e.g., "19L Damacana", "5L Damacana", "1.5L Şişe"
    
    private Integer page;
    
    private Integer size;
    
    private String sortBy; // "rating", "deliveryTime", "price", "name"
    
    private String sortOrder; // "asc", "desc"
    
    // Location-based filtering
    private Double latitude;
    
    private Double longitude;
    
    private Double radiusKm; // Search radius in kilometers
}

