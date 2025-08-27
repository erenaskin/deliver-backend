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
@Table(name = "pet_types")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String name; // e.g., "Kedi", "Köpek", "Kuş", "Balık"
    
    @Column(length = 100)
    private String emoji; // e.g., "🐱", "🐕", "🐦", "🐠"
    
    @Column(length = 500)
    private String description;
    
    @Column(length = 255)
    private String imageUrl;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;
    
    @Column(length = 50)
    private String color; // For UI styling
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private Service service;
    
    @OneToMany(mappedBy = "petType", fetch = FetchType.LAZY)
    private List<Product> products;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
