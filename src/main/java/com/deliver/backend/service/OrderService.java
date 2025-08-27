package com.deliver.backend.service;

import com.deliver.backend.dto.request.OrderRequest;
import com.deliver.backend.dto.response.OrderResponse;
import com.deliver.backend.entity.Order;
import com.deliver.backend.exception.BadRequestException;
import com.deliver.backend.exception.ResourceNotFoundException;
import com.deliver.backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        log.info("Fetching order with ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forOrder(id));
        return mapToResponse(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        log.info("Fetching all orders");
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest request, Long vendorId) {
        log.info("Creating new order for vendor ID: {}", vendorId);

        validateOrderRequest(request);

        Order order = Order.builder()
                .totalAmount(java.math.BigDecimal.ZERO) // Placeholder - would calculate from items
                .build();

        order = orderRepository.save(order);
        log.info("Order created successfully with ID: {}", order.getId());

        return mapToResponse(order);
    }

    private void validateOrderRequest(OrderRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new BadRequestException("Order must contain at least one item");
        }

        if (request.getDeliveryAddress() == null || request.getDeliveryAddress().trim().isEmpty()) {
            throw new BadRequestException("Delivery address is required");
        }
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        log.info("Fetching all orders with pagination");
        return orderRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Transactional
    public void cancelOrder(Long id, String reason) {
        log.info("Cancelling order with ID: {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forOrder(id));

        if (!canCancelOrder(order)) {
            throw new BadRequestException("Order cannot be cancelled at this stage");
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        order.setCancellationReason(reason);
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);
        log.info("Order {} cancelled successfully", id);
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long id, Order.OrderStatus status) {
        log.info("Updating order {} status to {}", id, status);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forOrder(id));

        if (!canTransitionToStatus(order.getStatus(), status)) {
            throw new BadRequestException("Invalid status transition from " + order.getStatus() + " to " + status);
        }

        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());

        if (status == Order.OrderStatus.DELIVERED) {
            order.setEstimatedDeliveryTime(LocalDateTime.now());
        }

        order = orderRepository.save(order);
        log.info("Order {} status updated to {}", id, status);

        return mapToResponse(order);
    }

    @Transactional(readOnly = true)
    public Object getOrderTracking(Long id) {
        log.info("Getting tracking information for order: {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forOrder(id));

        Map<String, Object> trackingInfo = new HashMap<>();
        trackingInfo.put("orderId", order.getId());
        trackingInfo.put("status", order.getStatus());
        trackingInfo.put("createdAt", order.getCreatedAt());
        trackingInfo.put("estimatedDeliveryTime", order.getEstimatedDeliveryTime());

        // Add tracking history based on status
        List<Map<String, Object>> history = createTrackingHistory(order);
        trackingInfo.put("history", history);

        return trackingInfo;
    }

    @Transactional
    public void rateOrder(Long id, Integer rating, String comment) {
        log.info("Rating order {} with rating {}", id, rating);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forOrder(id));

        if (order.getStatus() != Order.OrderStatus.DELIVERED) {
            throw new BadRequestException("Order must be delivered before rating");
        }

        if (rating < 1 || rating > 5) {
            throw new BadRequestException("Rating must be between 1 and 5");
        }

        // Save rating to a separate ratings table (in a real application)
        saveOrderRating(order, rating, comment);

        log.info("Order {} rated with {} stars", id, rating);
    }

    private void saveOrderRating(Order order, Integer rating, String comment) {
        // In a real application, you would:
        // 1. Create a Rating entity with orderId, userId, vendorId, rating, comment
        // 2. Save to ratings table
        // 3. Update vendor's average rating
        // 4. Send notification to vendor

        log.info("Saving rating for order {}: {} stars", order.getId(), rating);
        if (comment != null && !comment.trim().isEmpty()) {
            log.debug("Rating comment: {}", comment);
        }

        // Simulate updating vendor rating
        updateVendorRating(order.getVendorId(), rating);

        // In production, replace with actual rating service:
        // Rating ratingEntity = Rating.builder()
        //     .orderId(order.getId())
        //     .userId(order.getUserId())
        //     .vendorId(order.getVendorId())
        //     .rating(rating)
        //     .comment(comment)
        //     .createdAt(LocalDateTime.now())
        //     .build();
        // ratingRepository.save(ratingEntity);
    }

    private void updateVendorRating(Long vendorId, Integer newRating) {
        // In a real application, you would:
        // 1. Get vendor from repository
        // 2. Calculate new average rating
        // 3. Update vendor's rating fields

        log.debug("Updating vendor {} rating with new rating: {}", vendorId, newRating);

        // Simulate rating update
        // Vendor vendor = vendorRepository.findById(vendorId)...
        // BigDecimal currentAverage = vendor.getAverageRating();
        // int currentCount = vendor.getReviewCount();
        // BigDecimal newAverage = calculateNewAverage(currentAverage, currentCount, newRating);
        // vendor.setAverageRating(newAverage);
        // vendor.setReviewCount(currentCount + 1);
        // vendorRepository.save(vendor);
    }

    @Transactional(readOnly = true)
    public Object getOrderStatistics() {
        log.info("Getting order statistics");

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalOrders", orderRepository.count());
        statistics.put("pendingOrders", orderRepository.countByStatus(Order.OrderStatus.PENDING));
        statistics.put("deliveredOrders", orderRepository.countByStatus(Order.OrderStatus.DELIVERED));
        statistics.put("cancelledOrders", orderRepository.countByStatus(Order.OrderStatus.CANCELLED));

        return statistics;
    }

    private boolean canCancelOrder(Order order) {
        return order.getStatus() == Order.OrderStatus.PENDING ||
               order.getStatus() == Order.OrderStatus.CONFIRMED;
    }

    private boolean canTransitionToStatus(Order.OrderStatus currentStatus, Order.OrderStatus newStatus) {
        switch (currentStatus) {
            case PENDING:
                return newStatus == Order.OrderStatus.CONFIRMED ||
                       newStatus == Order.OrderStatus.CANCELLED;
            case CONFIRMED:
                return newStatus == Order.OrderStatus.PREPARING ||
                       newStatus == Order.OrderStatus.CANCELLED;
            case PREPARING:
                return newStatus == Order.OrderStatus.READY_FOR_DELIVERY;
            case READY_FOR_DELIVERY:
                return newStatus == Order.OrderStatus.OUT_FOR_DELIVERY;
            case OUT_FOR_DELIVERY:
                return newStatus == Order.OrderStatus.DELIVERED;
            case DELIVERED:
            case CANCELLED:
            case REJECTED:
                return false; // Final states
            default:
                return false;
        }
    }

    private List<Map<String, Object>> createTrackingHistory(Order order) {
        List<Map<String, Object>> history = new java.util.ArrayList<>();

        // Add status changes to history
        Map<String, Object> statusEntry = new HashMap<>();
        statusEntry.put("status", order.getStatus().toString());
        statusEntry.put("timestamp", order.getUpdatedAt() != null ? order.getUpdatedAt() : order.getCreatedAt());
        statusEntry.put("description", "Order " + order.getStatus().toString().toLowerCase());
        history.add(statusEntry);

        return history;
    }

    private OrderResponse mapToResponse(Order order) {
        List<OrderResponse.OrderItemResponse> itemResponses = List.of(); // Placeholder

        return OrderResponse.builder()
                .id(order.getId())
                .status(order.getStatus().toString())
                .items(itemResponses)
                .deliveryAddress(order.getDeliveryAddress())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
