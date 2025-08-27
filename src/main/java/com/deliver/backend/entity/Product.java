
package com.deliver.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(length = 50)
    private String category;

    @Column(nullable = false)
    private Integer quantity;

    @Builder.Default
    private Boolean isAvailable = true;

    @Column(nullable = false)
    private Long vendorId;

    @Column(length = 500)
    private String imageUrl;

    @Column(length = 50)
    private String sku;

    @Column(precision = 5, scale = 2)
    private BigDecimal weight;

    @Column(length = 10)
    private String weightUnit;

    @Column(length = 1000)
    private String ingredients;

    @Column(length = 500)
    private String allergens;

    @Builder.Default
    private Boolean isVegetarian = false;

    @Builder.Default
    private Boolean isVegan = false;

    @Builder.Default
    private Boolean isGlutenFree = false;

    @Builder.Default
    private Boolean isSpicy = false;

    private Integer preparationTimeMinutes;

    @Builder.Default
    private Integer sortOrder = 0;

    @Builder.Default
    private Boolean isFeatured = false;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}


