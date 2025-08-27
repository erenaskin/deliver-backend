package com.deliver.backend.controller;

import com.deliver.backend.dto.request.OrderRequest;
import com.deliver.backend.dto.response.OrderResponse;
import com.deliver.backend.entity.Order;
import com.deliver.backend.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Orders", description = "Order management endpoints")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Create new order", description = "Create a new order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Order created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid order data")
    })
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest orderRequest,
            @RequestParam Long vendorId) {

        OrderResponse response = orderService.createOrder(orderRequest, vendorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Retrieve order details by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order found"),
        @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        OrderResponse response = orderService.getOrderById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all orders", description = "Retrieve all orders")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    })
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> response = orderService.getAllOrders();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    @Operation(summary = "Get user orders", description = "Retrieve paginated list of user's orders")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<OrderResponse>> getUserOrders(
            @Parameter(description = "Pagination parameters") Pageable pageable,
            @RequestParam(required = false) String status,
            Authentication authentication) {

        // For now using service method that gets all orders - should be filtered by user
        Page<OrderResponse> orders = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel order", description = "Cancel an existing order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order cancelled successfully"),
        @ApiResponse(responseCode = "404", description = "Order not found"),
        @ApiResponse(responseCode = "400", description = "Order cannot be cancelled"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> cancelOrder(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "Cancelled by user") String reason) {
        orderService.cancelOrder(id, reason);
        return ResponseEntity.ok("Order cancelled successfully");
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update order status", description = "Update order status (Vendor/Admin only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order status updated successfully"),
        @ApiResponse(responseCode = "404", description = "Order not found"),
        @ApiResponse(responseCode = "400", description = "Invalid status transition"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('VENDOR') or hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status.toUpperCase());
        OrderResponse updatedOrder = orderService.updateOrderStatus(id, orderStatus);
        return ResponseEntity.ok(updatedOrder);
    }

    @GetMapping("/{id}/track")
    @Operation(summary = "Track order", description = "Get real-time order tracking information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tracking information retrieved"),
        @ApiResponse(responseCode = "404", description = "Order not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> trackOrder(@PathVariable Long id) {
        Object trackingInfo = orderService.getOrderTracking(id);
        return ResponseEntity.ok(trackingInfo);
    }

    @PostMapping("/{id}/rate")
    @Operation(summary = "Rate order", description = "Rate and review completed order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rating submitted successfully"),
        @ApiResponse(responseCode = "404", description = "Order not found"),
        @ApiResponse(responseCode = "400", description = "Order cannot be rated"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> rateOrder(
            @PathVariable Long id,
            @RequestParam Integer rating,
            @RequestParam(required = false) String comment) {

        orderService.rateOrder(id, rating, comment);
        return ResponseEntity.ok("Order rated successfully");
    }

    // Vendor-specific endpoints
    @GetMapping("/vendor")
    @Operation(summary = "Get vendor orders", description = "Retrieve orders for vendor's products")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vendor orders retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<Page<OrderResponse>> getVendorOrders(
            @Parameter(description = "Pagination parameters") Pageable pageable,
            @RequestParam(required = false) String status,
            Authentication authentication) {

        // For now using service method that gets all orders - should be filtered by vendor
        Page<OrderResponse> orders = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/statistics")
    @Operation(summary = "Get order statistics", description = "Get order statistics for current user/vendor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasRole('USER') or hasRole('VENDOR')")
    public ResponseEntity<Object> getOrderStatistics() {
        Object statistics = orderService.getOrderStatistics();
        return ResponseEntity.ok(statistics);
    }
}
