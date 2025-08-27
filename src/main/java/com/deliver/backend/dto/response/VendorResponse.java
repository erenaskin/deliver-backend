package com.deliver.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Vendor response payload")
public class VendorResponse {

    @Schema(description = "Vendor ID", example = "1")
    private Long id;

    @Schema(description = "Business name", example = "Mario's Pizza Restaurant")
    private String businessName;

    @Schema(description = "Business description", example = "Authentic Italian pizza made with fresh ingredients")
    private String description;

    @Schema(description = "Business category", example = "Restaurant")
    private String category;

    @Schema(description = "Business subcategory", example = "Pizza")
    private String subcategory;

    @Schema(description = "Business address", example = "456 Business Ave, City, State, ZIP")
    private String address;

    @Schema(description = "Phone number", example = "+1234567890")
    private String phoneNumber;

    @Schema(description = "Business email", example = "contact@mariospizza.com")
    private String businessEmail;

    @Schema(description = "Website URL", example = "https://www.mariospizza.com")
    private String websiteUrl;

    @Schema(description = "Business logo URL", example = "https://example.com/logo.jpg")
    private String logoUrl;

    @Schema(description = "Banner image URL", example = "https://example.com/banner.jpg")
    private String bannerImageUrl;

    @Schema(description = "Gallery images")
    private List<String> galleryImageUrls;

    @Schema(description = "Minimum order amount", example = "15.00")
    private BigDecimal minimumOrderAmount;

    @Schema(description = "Delivery fee", example = "2.99")
    private BigDecimal deliveryFee;

    @Schema(description = "Estimated delivery time in minutes", example = "30")
    private Integer estimatedDeliveryTimeMinutes;

    @Schema(description = "Delivery radius in kilometers", example = "5.0")
    private BigDecimal deliveryRadiusKm;

    @Schema(description = "Average rating", example = "4.2")
    private Double averageRating;

    @Schema(description = "Total number of reviews", example = "456")
    private Integer reviewCount;

    @Schema(description = "Total number of orders", example = "1234")
    private Integer totalOrders;

    @Schema(description = "Vendor status", example = "ACTIVE")
    private String status;

    @Schema(description = "Whether vendor is currently open", example = "true")
    private Boolean isOpen;

    @Schema(description = "Whether vendor is currently accepting orders", example = "true")
    private Boolean isAcceptingOrders;

    @Schema(description = "Distance from user in kilometers", example = "2.5")
    private Double distanceKm;

    @Schema(description = "Operating hours")
    private List<OperatingHoursResponse> operatingHours;

    @Schema(description = "Owner information")
    private OwnerInfo owner;

    @Schema(description = "Business statistics")
    private BusinessStats stats;

    @Schema(description = "Vendor registration time")
    private LocalDateTime createdAt;

    @Schema(description = "Last update time")
    private LocalDateTime updatedAt;

    @Schema(description = "Approval time")
    private LocalDateTime approvedAt;
    
    // Mobile app specific fields
    @Schema(description = "Vendor tags", example = "[\"VET\", \"BAKIM\", \"TAKSİT\", \"İNDİRİM\", \"REÇETE\", \"7/24\"]")
    private List<String> tags;
    
    @Schema(description = "Special feature", example = "Veteriner danışma")
    private String specialFeature;
    
    @Schema(description = "Whether vet service is available", example = "false")
    private Boolean isVetServiceAvailable;
    
    @Schema(description = "Whether vendor is 24/7", example = "false")
    private Boolean is24Hours;
    
    @Schema(description = "Whether prescription is available", example = "false")
    private Boolean isPrescriptionAvailable;
    
    @Schema(description = "Whether installment is available", example = "false")
    private Boolean isInstallmentAvailable;
    
    @Schema(description = "Whether vendor has discounts", example = "false")
    private Boolean hasDiscount;
    
    @Schema(description = "Discount text", example = "Seçili ürünlerde %25'e varan indirim")
    private String discountText;
    
    @Schema(description = "Installment text", example = "12 aya varan taksit")
    private String installmentText;
    
    @Schema(description = "Warranty text", example = "Resmi garanti")
    private String warrantyText;
    
    @Schema(description = "Consultation text", example = "Veteriner danışma")
    private String consultationText;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Operating hours response")
    public static class OperatingHoursResponse {

        @Schema(description = "Day of week (1=Monday, 7=Sunday)", example = "1")
        private Integer dayOfWeek;

        @Schema(description = "Day name", example = "Monday")
        private String dayName;

        @Schema(description = "Opening time", example = "09:00")
        private LocalTime openTime;

        @Schema(description = "Closing time", example = "22:00")
        private LocalTime closeTime;

        @Schema(description = "Whether closed on this day", example = "false")
        private Boolean isClosed;

        @Schema(description = "Whether currently open", example = "true")
        private Boolean isCurrentlyOpen;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Owner information")
    public static class OwnerInfo {

        @Schema(description = "Owner ID", example = "1")
        private Long id;

        @Schema(description = "Owner name", example = "Mario Rossi")
        private String name;

        @Schema(description = "Owner email", example = "mario@mariospizza.com")
        private String email;

        @Schema(description = "Join date")
        private LocalDateTime joinedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Business statistics")
    public static class BusinessStats {

        @Schema(description = "Total products", example = "25")
        private Integer totalProducts;

        @Schema(description = "Active products", example = "23")
        private Integer activeProducts;

        @Schema(description = "Orders this month", example = "156")
        private Integer ordersThisMonth;

        @Schema(description = "Revenue this month", example = "3245.67")
        private BigDecimal revenueThisMonth;

        @Schema(description = "Average order value", example = "28.50")
        private BigDecimal averageOrderValue;

        @Schema(description = "Customer satisfaction rate", example = "94.5")
        private Double customerSatisfactionRate;

        @Schema(description = "On-time delivery rate", example = "96.2")
        private Double onTimeDeliveryRate;
    }
}
