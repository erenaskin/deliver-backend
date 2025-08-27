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
@Table(name = "product_categories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String name; // e.g., "Tümü", "Ağrı Kesici", "Soğuk Algınlığı", "Vit"
    
    @Column(length = 100)
    private String displayName; // Full name if abbreviated
    
    @Column(length = 500)
    private String description;
    
    @Column(length = 100)
    private String iconName;
    
    @Column(length = 50)
    private String color; // For selected state
    
    @Column(length = 50)
    private String backgroundColor; // For selected state
    
    @Column(length = 50)
    private String textColor; // For selected state
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean isDefault = false; // For "Tümü" category
    
    @Column(nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;
    
    @Column(length = 100)
    private String searchKeyword; // For search functionality
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_category_id", nullable = false)
    private ServiceCategory serviceCategory;
    
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Product> products;
}
