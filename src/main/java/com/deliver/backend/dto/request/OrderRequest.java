package com.deliver.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @NotNull(message = "Payment method is required")
    @Schema(description = "Payment method", example = "CREDIT_CARD")
    private String paymentMethod;

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
}
