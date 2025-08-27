package com.deliver.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "service_categories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCategory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100, unique = true)
    private String name; // e.g., "Eczane", "Pet", "Food", "Su", "Market", "Teknoloji"
    
    @Column(length = 100)
    private String displayName; // e.g., "DeliVer Eczane", "Deliver Pet", "DeliVer Food"
    
    @Column(length = 500)
    private String description;
    
    @Column(length = 100)
    private String subtitle; // e.g., "İlaç ve sağlık ürünleri", "Evcil hayvan ürünleri"
    
    @Column(length = 100)
    private String iconName; // e.g., "pill", "dog", "pizza", "water-drop", "shopping-cart", "smartphone"
    
    @Column(length = 500)
    private String imageUrl;
    
    @Column(length = 500)
    private String bannerImageUrl;
    
    @Column(length = 50)
    private String color; // Primary color for the service (e.g., "#FF0000" for red)
    
    @Column(length = 50)
    private String accentColor; // Secondary color for the service
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean isEmergencyService = false; // For pharmacy emergency services
    
    @Column(length = 100)
    private String emergencyText; // e.g., "Acil Durum? 7/24 nöbetçi eczanelere ulaşın"
    
    @Column(length = 100)
    private String emergencyButtonText; // e.g., "Ara"
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    // Relationships
    @OneToMany(mappedBy = "serviceCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Service> services;
    
    @OneToMany(mappedBy = "serviceCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuickCategory> quickCategories;
    
    @OneToMany(mappedBy = "serviceCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductCategory> productCategories;
}

