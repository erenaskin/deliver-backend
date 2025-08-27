package com.deliver.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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

    @Column(nullable = false, length = 500)
    private String address;

    @Column(nullable = false, length = 20)
    private String phoneNumber;
    
    @Column(length = 20)
    private String phone;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(nullable = false)
    private BigDecimal deliveryFee;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal deliveryRadiusKm;

    @Column(nullable = false)
    private Integer estimatedDeliveryTimeMinutes;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isAcceptingOrders = true;

    @Column(nullable = false)
    private BigDecimal minimumOrderAmount;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Mobile UI specific fields
    @ElementCollection
    @CollectionTable(name = "vendor_tags", joinColumns = @JoinColumn(name = "vendor_id"))
    @Column(name = "tag")
    private Set<String> tags; // e.g., "VET", "BAKIM", "TAKSİT", "İNDİRİM", "REÇETE", "7/24"
    
    @Column(length = 100)
    private String specialFeature; // e.g., "Veteriner danışma", "Resmi garanti", "12 aya varan taksit"
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean isVetServiceAvailable = false; // For DeliVer Pet
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean is24Hours = false; // "7/24" tag
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean isPrescriptionAvailable = false; // "REÇETE" tag for pharmacies
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean isInstallmentAvailable = false; // "TAKSİT" tag
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean hasDiscount = false; // "İNDİRİM" tag
    
    @Column(length = 100)
    private String discountText; // e.g., "Seçili ürünlerde %25'e varan indirim"
    
    @Column(length = 100)
    private String installmentText; // e.g., "12 aya varan taksit"
    
    @Column(length = 100)
    private String warrantyText; // e.g., "Resmi garanti"
    
    @Column(length = 100)
    private String consultationText; // e.g., "Veteriner danışma"
    
    // Additional mobile app fields
    @Column(length = 100)
    private String serviceType; // e.g., "Eczane", "Pet", "Food", "Su", "Market", "Teknoloji"

    @Column(length = 100)
    private String websiteUrl;
    
    @Column(length = 100)
    private String website;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean isVerified = false;
    
    @Column(length = 100)
    private String deliveryArea;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean isFreeDelivery = false;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean isFavorite = false;
    
    @Column(length = 200)
    private String businessHours;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean isOpenNow = true;
    
    @Column(length = 100)
    private String nextOpenTime;

    @Column(length = 255)
    private String bannerImageUrl;

    @Column(length = 100)
    private String taxId;

    @Column(length = 100)
    private String businessLicenseNumber;

    @Builder.Default
    private Integer totalOrders = 0;

    private LocalDateTime approvedAt;

    @Column(length = 255)
    private String rejectionReason;

    @Column(length = 100)
    private String subcategory;

    @Column(length = 100)
    private String businessEmail;
    
    @Column(length = 100)
    private String email;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private VendorStatus status = VendorStatus.ACTIVE;

    @Column(length = 255)
    private String logoUrl;

    @Builder.Default
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Builder.Default
    private Integer reviewCount = 0;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private Service service;
    
    // ServiceCategory relationship removed as per simplification

    // Helper methods
    public String getWebsiteUrl() { return websiteUrl; }
    public void setWebsiteUrl(String websiteUrl) { this.websiteUrl = websiteUrl; }
    public String getBannerImageUrl() { return bannerImageUrl; }
    public void setBannerImageUrl(String bannerImageUrl) { this.bannerImageUrl = bannerImageUrl; }
    public String getTaxId() { return taxId; }
    public void setTaxId(String taxId) { this.taxId = taxId; }
    public String getBusinessLicenseNumber() { return businessLicenseNumber; }
    public void setBusinessLicenseNumber(String businessLicenseNumber) { this.businessLicenseNumber = businessLicenseNumber; }
    public Integer getTotalOrders() { return totalOrders; }
    public void setTotalOrders(Integer totalOrders) { this.totalOrders = totalOrders; }
    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
    public String getSubcategory() { return subcategory; }
    public void setSubcategory(String subcategory) { this.subcategory = subcategory; }
    public void updateRating(BigDecimal newRating, int reviewCount) { this.averageRating = newRating; this.reviewCount = reviewCount; }
    public void setStatus(VendorStatus status) { this.status = status; }

    public String getBusinessEmail() {
        return businessEmail;
    }

    public String getDescription() {
        return description;
    }

    public VendorStatus getStatus() {
        return status;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public BigDecimal getAverageRating() {
        return averageRating;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public enum VendorStatus {
        ACTIVE,
        INACTIVE,
        SUSPENDED,
        DELETED,
        PENDING,
        REJECTED
    }
}
