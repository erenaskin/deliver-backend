package com.deliver.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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

    @AssertTrue(message = "You must accept the vendor terms and conditions")
    @Schema(description = "Vendor terms acceptance", example = "true")
    private Boolean acceptVendorTerms;
}
