package com.deliver.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Vendor registration request payload")
public class VendorRequest {

    @NotBlank(message = "Business name is required")
    @Size(min = 2, max = 100, message = "Business name must be between 2 and 100 characters")
    @Schema(description = "Business name", example = "Mario's Pizza Restaurant")
    private String businessName;

    @NotBlank(message = "Business description is required")
    @Size(min = 10, max = 1000, message = "Business description must be between 10 and 1000 characters")
    @Schema(description = "Business description", example = "Authentic Italian pizza made with fresh ingredients")
    private String description;

    @NotBlank(message = "Business category is required")
    @Schema(description = "Business category", example = "Restaurant")
    private String category;

    @Schema(description = "Business subcategories", example = "[\"Pizza\", \"Italian\", \"Fast Food\"]")
    private List<String> subcategories;

    @NotBlank(message = "Business address is required")
    @Size(max = 500, message = "Business address must not exceed 500 characters")
    @Schema(description = "Business address", example = "456 Business Ave, City, State, ZIP")
    private String address;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number must be valid")
    @Schema(description = "Business phone number", example = "+1234567890")
    private String phoneNumber;

    @Email(message = "Business email must be valid")
    @Schema(description = "Business email", example = "contact@mariospizza.com")
    private String businessEmail;

    @Schema(description = "Business website URL", example = "https://www.mariospizza.com")
    private String websiteUrl;

    @NotNull(message = "Minimum order amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Minimum order amount must be greater than 0")
    @Schema(description = "Minimum order amount", example = "15.00")
    private BigDecimal minimumOrderAmount;

    @NotNull(message = "Delivery fee is required")
    @DecimalMin(value = "0.0", message = "Delivery fee must be 0 or greater")
    @Schema(description = "Delivery fee", example = "2.99")
    private BigDecimal deliveryFee;

    @NotNull(message = "Estimated delivery time is required")
    @Min(value = 5, message = "Estimated delivery time must be at least 5 minutes")
    @Max(value = 120, message = "Estimated delivery time must not exceed 120 minutes")
    @Schema(description = "Estimated delivery time in minutes", example = "30")
    private Integer estimatedDeliveryTimeMinutes;

    @NotNull(message = "Delivery radius is required")
    @DecimalMin(value = "0.1", message = "Delivery radius must be at least 0.1 km")
    @DecimalMax(value = "50.0", message = "Delivery radius must not exceed 50 km")
    @Schema(description = "Delivery radius in kilometers", example = "5.0")
    private BigDecimal deliveryRadiusKm;

    @Schema(description = "Business operating hours")
    private List<OperatingHours> operatingHours;

    @Schema(description = "Business logo URL", example = "https://example.com/logo.jpg")
    private String logoUrl;

    @Schema(description = "Business banner image URL", example = "https://example.com/banner.jpg")
    private String bannerImageUrl;

    @Schema(description = "Business gallery images")
    private List<String> galleryImageUrls;

    @NotBlank(message = "Tax ID is required")
    @Schema(description = "Business tax identification number", example = "12-3456789")
    private String taxId;

    @NotBlank(message = "Business license number is required")
    @Schema(description = "Business license number", example = "BL-123456")
    private String businessLicenseNumber;

    @AssertTrue(message = "You must accept the vendor terms and conditions")
    @Schema(description = "Vendor terms acceptance", example = "true")
    private Boolean acceptVendorTerms;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Operating hours for a specific day")
    public static class OperatingHours {

        @NotNull(message = "Day of week is required")
        @Min(value = 1, message = "Day of week must be between 1 (Monday) and 7 (Sunday)")
        @Max(value = 7, message = "Day of week must be between 1 (Monday) and 7 (Sunday)")
        @Schema(description = "Day of week (1=Monday, 7=Sunday)", example = "1")
        private Integer dayOfWeek;

        @Schema(description = "Opening time", example = "09:00")
        private java.time.LocalTime openTime;

        @Schema(description = "Closing time", example = "22:00")
        private java.time.LocalTime closeTime;

        @Schema(description = "Whether the business is closed on this day", example = "false")
        @Builder.Default
        private Boolean isClosed = false;
    }
}
