package com.deliver.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(precision = 10, scale = 2)
    private BigDecimal originalPrice;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(length = 50)
    private String subcategory;

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> imageUrls;

    @Column(length = 255)
    private String mainImageUrl;

    @Column(nullable = false)
    @Builder.Default
    private Integer quantity = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer minOrderQuantity = 1;

    private Integer maxOrderQuantity;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isAvailable = true;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isFeatured = false;

    private Integer preparationTimeMinutes;

    @ElementCollection
    @CollectionTable(name = "product_tags", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "tag")
    private Set<String> tags;

    @Column(precision = 3, scale = 2)
    @Builder.Default
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Builder.Default
    private Integer reviewCount = 0;

    @Builder.Default
    private Integer totalSales = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ProductStatus status = ProductStatus.ACTIVE;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Mobile UI specific fields
    @Column(length = 100)
    private String volume; // e.g., "19L Damacana", "5L Damacana", "1.5L Şişe"
    
    @Column(length = 100)
    private String deliverySpeedText; // e.g., "Hızlı teslimat"
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean isAvailableToday = true; // "Bugün teslimat mümkün"
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean isPopular = false; // "POPÜLER" tag
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean isNewArrival = false; // Red dot indicator
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean isPromotion = false; // Orange dot indicator
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean isLimitedStock = false; // Green dot indicator
    
    @Column(length = 100)
    private String brand; // e.g., "Erikli", "Hayat Su", "Pınar Su"
    
    @Column(precision = 10, scale = 2)
    private BigDecimal deliveryFee;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer deliveryTimeMinutes = 30; // Default delivery time
    
    // Additional mobile app fields
    @Column(length = 100)
    private String waterType; // e.g., "19L Damacana", "5L Damacana", "1.5L Şişe"

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private Service service;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_category_id")
    private ProductCategory productCategory;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_type_id")
    private PetType petType; // For DeliVer Pet products

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ProductNutritionalInfo nutritionalInfo;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductVariant> variants;

    @ManyToMany(mappedBy = "favoriteProducts", fetch = FetchType.LAZY)
    private Set<User> favoritedByUsers;

    // Helper methods
    public boolean isInStock() {
        return quantity > 0 && isAvailable && status == ProductStatus.ACTIVE;
    }

    public boolean hasDiscount() {
        return originalPrice != null && originalPrice.compareTo(price) > 0;
    }

    public BigDecimal getDiscountPercentage() {
        if (!hasDiscount()) {
            return BigDecimal.ZERO;
        }
        BigDecimal discount = originalPrice.subtract(price);
        return discount.divide(originalPrice, 4, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    public BigDecimal getDiscountAmount() {
        if (!hasDiscount()) {
            return BigDecimal.ZERO;
        }
        return originalPrice.subtract(price);
    }

    public void updateRating(BigDecimal newRating, int newReviewCount) {
        this.averageRating = newRating;
        this.reviewCount = newReviewCount;
    }

    public void incrementSales(int quantity) {
        this.totalSales += quantity;
    }

    public void decrementQuantity(int orderQuantity) {
        if (this.quantity >= orderQuantity) {
            this.quantity -= orderQuantity;
        }
    }

    public void incrementQuantity(int addedQuantity) {
        this.quantity += addedQuantity;
    }

    // Enums
    public enum ProductStatus {
        ACTIVE, INACTIVE, OUT_OF_STOCK, DISCONTINUED
    }
}


