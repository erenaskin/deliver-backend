package com.deliver.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Order response payload")
public class OrderResponse {

    @Schema(description = "Order ID", example = "1")
    private Long id;

    @Schema(description = "Order status", example = "CONFIRMED")
    private String status;

    @Schema(description = "Order items")
    private List<OrderItemResponse> items;

    @Schema(description = "Delivery address", example = "123 Main St, Apt 4B, City, State, ZIP")
    private String deliveryAddress;

    @Schema(description = "Total amount", example = "33.77")
    private BigDecimal totalAmount;

    @Schema(description = "Order creation time")
    private LocalDateTime createdAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Order item response")
    public static class OrderItemResponse {

        @Schema(description = "Product ID", example = "1")
        private Long productId;

        @Schema(description = "Product name", example = "Margherita Pizza")
        private String productName;

        @Schema(description = "Item quantity", example = "2")
        private Integer quantity;

        @Schema(description = "Unit price at time of order", example = "12.99")
        private BigDecimal unitPrice;

        @Schema(description = "Special instructions", example = "Extra cheese, no onions")
        private String specialInstructions;
    }
}
