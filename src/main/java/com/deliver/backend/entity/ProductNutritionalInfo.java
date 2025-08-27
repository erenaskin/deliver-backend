package com.deliver.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "product_nutritional_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductNutritionalInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer calories;

    @Column(precision = 8, scale = 2)
    private BigDecimal protein;

    @Column(precision = 8, scale = 2)
    private BigDecimal carbohydrates;

    @Column(precision = 8, scale = 2)
    private BigDecimal fat;

    @Column(precision = 8, scale = 2)
    private BigDecimal fiber;

    @Column(precision = 8, scale = 2)
    private BigDecimal sugar;

    private Integer sodium;

    @ElementCollection
    @CollectionTable(name = "product_allergens", joinColumns = @JoinColumn(name = "nutritional_info_id"))
    @Column(name = "allergen")
    private Set<String> allergens;
}

