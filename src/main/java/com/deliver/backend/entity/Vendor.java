
package com.deliver.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vendors")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String businessName;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(length = 50)
    private String subcategory;

    @Column(length = 500)
    private String address;

    @Column(length = 20)
    private String phoneNumber;

    @Column(length = 100)
    private String businessEmail;

    @Column(length = 200)
    private String websiteUrl;

    @Column(length = 500)
    private String logoUrl;

    @Column(length = 500)
    private String bannerImageUrl;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private VendorStatus status = VendorStatus.PENDING;

    @Builder.Default
    private Boolean isAcceptingOrders = true;

    @Builder.Default
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Builder.Default
    private Integer reviewCount = 0;

    @Builder.Default
    private Integer totalOrders = 0;

    @Column(precision = 10, scale = 2)
    private BigDecimal minimumOrderAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal deliveryFee;

    private Integer estimatedDeliveryTimeMinutes;

    @Column(precision = 5, scale = 2)
    private BigDecimal deliveryRadiusKm;

    @Column(length = 50)
    private String taxId;

    @Column(length = 100)
    private String businessLicenseNumber;

    private LocalDateTime approvedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Column(length = 500)
    private String rejectionReason;

    public enum VendorStatus {
        PENDING, ACTIVE, INACTIVE, REJECTED
    }

    public void updateRating(BigDecimal newRating, int reviewCount) {
        this.averageRating = newRating;
        this.reviewCount = reviewCount;
    }
}
