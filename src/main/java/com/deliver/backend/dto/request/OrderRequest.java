package com.deliver.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
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
@Schema(description = "Order creation request payload")
public class OrderRequest {

    @NotEmpty(message = "Order items cannot be empty")
    @Valid
    @Schema(description = "List of items to order")
    private List<OrderItemRequest> items;

    @NotBlank(message = "Delivery address is required")
    @Size(max = 500, message = "Delivery address must not exceed 500 characters")
    @Schema(description = "Delivery address", example = "123 Main St, Apt 4B, City, State, ZIP")
    private String deliveryAddress;

    @Schema(description = "Delivery coordinates")
    private DeliveryCoordinates deliveryCoordinates;

    @Size(max = 500, message = "Order notes must not exceed 500 characters")
    @Schema(description = "Special instructions or notes", example = "Ring doorbell, leave at door")
    private String notes;

    @NotNull(message = "Payment method is required")
    @Schema(description = "Payment method", example = "CREDIT_CARD")
    private String paymentMethod;

    @Schema(description = "Scheduled delivery time (optional)")
    private java.time.LocalDateTime scheduledDeliveryTime;

    @Schema(description = "Tip amount", example = "5.00")
    private BigDecimal tipAmount;

    @Schema(description = "Promo code", example = "SAVE20")
    private String promoCode;

    @NotNull(message = "Vendor ID is required")
    @Schema(description = "Vendor ID", example = "1")
    private Long vendorId;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Order item request")
    public static class OrderItemRequest {

        @NotNull(message = "Product ID is required")
        @Schema(description = "Product ID", example = "1")
        private Long productId;

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        @Max(value = 100, message = "Quantity must not exceed 100")
        @Schema(description = "Item quantity", example = "2")
        private Integer quantity;

        @Size(max = 255, message = "Special instructions must not exceed 255 characters")
        @Schema(description = "Special instructions for this item", example = "Extra cheese, no onions")
        private String specialInstructions;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Delivery coordinates")
    public static class DeliveryCoordinates {

        @NotNull(message = "Latitude is required")
        @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
        @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
        @Schema(description = "Latitude coordinate", example = "40.7128")
        private BigDecimal latitude;

        @NotNull(message = "Longitude is required")
        @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
        @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
        @Schema(description = "Longitude coordinate", example = "-74.0060")
        private BigDecimal longitude;
    }
}
