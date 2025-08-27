package com.deliver.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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

    @Schema(description = "Business address", example = "456 Business Ave, City, State, ZIP")
    private String address;

    @Schema(description = "Phone number", example = "+1234567890")
    private String phoneNumber;

    @Schema(description = "Business email", example = "contact@mariospizza.com")
    private String businessEmail;

    @Schema(description = "Minimum order amount", example = "15.00")
    private BigDecimal minimumOrderAmount;

    @Schema(description = "Delivery fee", example = "2.99")
    private BigDecimal deliveryFee;

    @Schema(description = "Estimated delivery time in minutes", example = "30")
    private Integer estimatedDeliveryTimeMinutes;

    @Schema(description = "Delivery radius in kilometers", example = "5.0")
    private BigDecimal deliveryRadiusKm;

    @Schema(description = "Whether vendor is currently open", example = "true")
    private Boolean isOpen;
}
