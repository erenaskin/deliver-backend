package com.deliver.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "emergency_services")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyService {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String name; // e.g., "7/24 Nöbetçi Eczane"
    
    @Column(length = 500)
    private String description;
    
    @Column(length = 100)
    private String emergencyText; // e.g., "Acil Durum? 7/24 nöbetçi eczanelere ulaşın"
    
    @Column(length = 100)
    private String buttonText; // e.g., "Ara"
    
    @Column(length = 50)
    private String backgroundColor; // e.g., "#FFE6E6" for light red
    
    @Column(length = 50)
    private String borderColor; // e.g., "#FF0000" for red
    
    @Column(length = 100)
    private String iconName; // e.g., "warning-triangle"
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;
    
    @Column(length = 100)
    private String phoneNumber; // For emergency calls
    
    @Column(length = 500)
    private String actionUrl; // For emergency actions
    
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
}

