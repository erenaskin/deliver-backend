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
@Table(name = "quick_categories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuickCategory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String name; // e.g., "Ağrı Kesici", "Vitamin", "Soğuk Algınlığı"
    
    @Column(length = 100)
    private String emoji; // e.g., "💊", "🍊", "🤧"
    
    @Column(length = 100)
    private String iconName; // Alternative to emoji for web usage
    
    @Column(length = 50)
    private String backgroundColor; // e.g., "#FFE6E6" for light red
    
    @Column(length = 50)
    private String textColor; // e.g., "#000000" for black
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;
    
    @Column(length = 100)
    private String searchKeyword; // For search functionality
    
    @Column(length = 500)
    private String description;
    
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

