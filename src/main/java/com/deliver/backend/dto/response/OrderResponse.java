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

    @Schema(description = "Order number", example = "ORD-2023-001234")
    private String orderNumber;

    @Schema(description = "Order status", example = "CONFIRMED")
    private String status;

    @Schema(description = "Order items")
    private List<OrderItemResponse> items;

    @Schema(description = "Customer information")
    private CustomerInfo customer;

    @Schema(description = "Vendor information")
    private VendorInfo vendor;

    @Schema(description = "Delivery information")
    private DeliveryInfo delivery;

    @Schema(description = "Payment information")
    private PaymentInfo payment;

    @Schema(description = "Order totals")
    private OrderTotals totals;

    @Schema(description = "Order notes", example = "Ring doorbell, leave at door")
    private String notes;

    @Schema(description = "Order creation time")
    private LocalDateTime createdAt;

    @Schema(description = "Order last update time")
    private LocalDateTime updatedAt;

    @Schema(description = "Estimated delivery time")
    private LocalDateTime estimatedDeliveryTime;

    @Schema(description = "Actual delivery time")
    private LocalDateTime actualDeliveryTime;

    @Schema(description = "Order rating (1-5)", example = "4")
    private Integer rating;

    @Schema(description = "Order review comment")
    private String reviewComment;

    @Schema(description = "Order tracking information")
    private List<OrderTrackingInfo> trackingHistory;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Order item response")
    public static class OrderItemResponse {

        @Schema(description = "Order item ID", example = "1")
        private Long id;

        @Schema(description = "Product information")
        private ProductInfo product;

        @Schema(description = "Item quantity", example = "2")
        private Integer quantity;

        @Schema(description = "Unit price at time of order", example = "12.99")
        private BigDecimal unitPrice;

        @Schema(description = "Total price for this item", example = "25.98")
        private BigDecimal totalPrice;

        @Schema(description = "Special instructions", example = "Extra cheese, no onions")
        private String specialInstructions;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Product information in order")
    public static class ProductInfo {

        @Schema(description = "Product ID", example = "1")
        private Long id;

        @Schema(description = "Product name", example = "Margherita Pizza")
        private String name;

        @Schema(description = "Product image URL", example = "https://example.com/pizza.jpg")
        private String imageUrl;

        @Schema(description = "Product category", example = "Pizza")
        private String category;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Customer information")
    public static class CustomerInfo {

        @Schema(description = "Customer ID", example = "1")
        private Long id;

        @Schema(description = "Customer name", example = "John Doe")
        private String name;

        @Schema(description = "Customer phone", example = "+1234567890")
        private String phone;

        @Schema(description = "Customer email", example = "john.doe@example.com")
        private String email;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Vendor information")
    public static class VendorInfo {

        @Schema(description = "Vendor ID", example = "1")
        private Long id;

        @Schema(description = "Vendor name", example = "Mario's Pizza")
        private String name;

        @Schema(description = "Vendor phone", example = "+1234567890")
        private String phone;

        @Schema(description = "Vendor logo URL", example = "https://example.com/logo.jpg")
        private String logoUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Delivery information")
    public static class DeliveryInfo {

        @Schema(description = "Delivery address", example = "123 Main St, Apt 4B, City, State, ZIP")
        private String address;

        @Schema(description = "Delivery latitude", example = "40.7128")
        private BigDecimal latitude;

        @Schema(description = "Delivery longitude", example = "-74.0060")
        private BigDecimal longitude;

        @Schema(description = "Delivery method", example = "HOME_DELIVERY")
        private String method;

        @Schema(description = "Delivery fee", example = "2.99")
        private BigDecimal fee;

        @Schema(description = "Estimated delivery time in minutes", example = "30")
        private Integer estimatedTimeMinutes;

        @Schema(description = "Delivery driver information")
        private DriverInfo driver;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Driver information")
    public static class DriverInfo {

        @Schema(description = "Driver ID", example = "1")
        private Long id;

        @Schema(description = "Driver name", example = "Mike Johnson")
        private String name;

        @Schema(description = "Driver phone", example = "+1234567890")
        private String phone;

        @Schema(description = "Vehicle information", example = "Red Honda Civic - ABC123")
        private String vehicle;

        @Schema(description = "Driver rating", example = "4.8")
        private Double rating;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Payment information")
    public static class PaymentInfo {

        @Schema(description = "Payment method", example = "CREDIT_CARD")
        private String method;

        @Schema(description = "Payment status", example = "PAID")
        private String status;

        @Schema(description = "Transaction ID", example = "txn_123456789")
        private String transactionId;

        @Schema(description = "Payment time")
        private LocalDateTime paidAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Order totals")
    public static class OrderTotals {

        @Schema(description = "Subtotal (items cost)", example = "25.98")
        private BigDecimal subtotal;

        @Schema(description = "Delivery fee", example = "2.99")
        private BigDecimal deliveryFee;

        @Schema(description = "Tip amount", example = "5.00")
        private BigDecimal tip;

        @Schema(description = "Tax amount", example = "2.80")
        private BigDecimal tax;

        @Schema(description = "Discount amount", example = "3.00")
        private BigDecimal discount;

        @Schema(description = "Total amount", example = "33.77")
        private BigDecimal total;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Order tracking information")
    public static class OrderTrackingInfo {

        @Schema(description = "Status", example = "CONFIRMED")
        private String status;

        @Schema(description = "Status description", example = "Your order has been confirmed")
        private String description;

        @Schema(description = "Timestamp")
        private LocalDateTime timestamp;

        @Schema(description = "Location", example = "Mario's Pizza Restaurant")
        private String location;
    }
}
