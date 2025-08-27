package com.deliver.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_favorites")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFavorite {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_category_id")
    private ServiceCategory serviceCategory;
    
    @Column(nullable = false, length = 50)
    private String favoriteType; // "VENDOR", "PRODUCT", "SERVICE_CATEGORY"
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // Ensure only one entity is set per favorite
    @PrePersist
    @PreUpdate
    private void validateFavorite() {
        int count = 0;
        if (vendor != null) count++;
        if (product != null) count++;
        if (serviceCategory != null) count++;
        
        if (count != 1) {
            throw new IllegalStateException("Exactly one entity must be set for a favorite");
        }
    }
}
