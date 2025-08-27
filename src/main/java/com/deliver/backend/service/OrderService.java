package com.deliver.backend.service;

import com.deliver.backend.dto.request.OrderRequest;
import com.deliver.backend.dto.response.OrderResponse;
import com.deliver.backend.entity.Order;
import com.deliver.backend.entity.User;
import com.deliver.backend.entity.Vendor;
import com.deliver.backend.exception.BadRequestException;
import com.deliver.backend.exception.ResourceNotFoundException;
import com.deliver.backend.repository.OrderRepository;
import com.deliver.backend.repository.UserRepository;
import com.deliver.backend.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final VendorRepository vendorRepository;

    @Transactional(readOnly = true)
    public List<OrderResponse> getUserOrders(Long userId) {
        log.info("Fetching orders for user ID: {}", userId);
        return orderRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getVendorOrders(Long vendorId) {
        log.info("Fetching orders for vendor ID: {}", vendorId);
        return orderRepository.findByVendorId(vendorId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        log.info("Fetching order with ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forOrder(id));
        return mapToResponse(order);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        log.info("Fetching all orders");
        return orderRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest request, Long userId, Long vendorId) {
        log.info("Creating new order for user ID: {}", userId);

        validateOrderRequest(request);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> ResourceNotFoundException.forUser(userId));

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> ResourceNotFoundException.forVendor(vendorId));

        BigDecimal deliveryFee = vendor.getDeliveryFee() != null ? vendor.getDeliveryFee() : BigDecimal.ZERO;

        // Parse payment method
        Order.PaymentMethod paymentMethod;
        try {
            paymentMethod = Order.PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase());
        } catch (IllegalArgumentException e) {
            paymentMethod = Order.PaymentMethod.CASH; // Default fallback
        }

    Order order = Order.builder()
        .user(user)
        .vendor(vendor)
        .subtotal(BigDecimal.ZERO) // Will be calculated when items are added
        .deliveryFee(deliveryFee)
        .total(deliveryFee) // Only delivery fee for now
        .totalAmount(deliveryFee) // Zorunlu alan
        .status(Order.OrderStatus.PENDING)
        .paymentMethod(paymentMethod)
        .paymentStatus(Order.PaymentStatus.UNPAID)
        .deliveryAddress(request.getDeliveryAddress())
        .notes(request.getNotes())
        .estimatedDeliveryTime(LocalDateTime.now().plusMinutes(30))
        .promoCode(request.getPromoCode())
        .tip(request.getTipAmount() != null ? request.getTipAmount() : BigDecimal.ZERO)
        .latitude(request.getDeliveryCoordinates() != null ? request.getDeliveryCoordinates().getLatitude().doubleValue() : null)
        .longitude(request.getDeliveryCoordinates() != null ? request.getDeliveryCoordinates().getLongitude().doubleValue() : null)
        .scheduledDeliveryTime(request.getScheduledDeliveryTime())
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();

        order = orderRepository.save(order);
        log.info("Order created successfully with ID: {}", order.getId());

        return mapToResponse(order);
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, Order.OrderStatus newStatus) {
        log.info("Updating order {} status to {}", orderId, newStatus);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> ResourceNotFoundException.forOrder(orderId));

        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());

        if (newStatus == Order.OrderStatus.CONFIRMED) {
            order.setConfirmedAt(LocalDateTime.now());
        } else if (newStatus == Order.OrderStatus.DELIVERED) {
            order.setDeliveredAt(LocalDateTime.now());
        } else if (newStatus == Order.OrderStatus.CANCELLED) {
            order.setCancelledAt(LocalDateTime.now());
        }

        order = orderRepository.save(order);
        log.info("Order status updated successfully: {}", orderId);

        return mapToResponse(order);
    }

    @Transactional
    public void cancelOrder(Long orderId, String reason) {
        log.info("Cancelling order with ID: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> ResourceNotFoundException.forOrder(orderId));

        if (order.getStatus() != null && order.getStatus().equals(Order.OrderStatus.DELIVERED.name())) {
            throw new BadRequestException("Cannot cancel delivered order");
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        order.setCancelledAt(LocalDateTime.now());
        order.setCancellationReason(reason);
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);
        log.info("Order cancelled successfully: {}", orderId);
    }

    private void validateOrderRequest(OrderRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new BadRequestException("Order must contain at least one item");
        }

        if (request.getDeliveryAddress() == null || request.getDeliveryAddress().trim().isEmpty()) {
            throw new BadRequestException("Delivery address is required");
        }
    }

    private OrderResponse mapToResponse(Order order) {
    List<OrderResponse.OrderItemResponse> itemResponses = Collections.emptyList();

    OrderResponse.VendorInfo vendorInfo = OrderResponse.VendorInfo.builder()
            .id(order.getVendor().getId())
            .name(order.getVendor().getBusinessName())
            .phone(order.getVendor().getPhoneNumber())
            .build();

    OrderResponse.OrderTotals totals = OrderResponse.OrderTotals.builder()
            .subtotal(order.getSubtotal())
            .deliveryFee(order.getDeliveryFee())
            .tip(BigDecimal.ZERO)
            .tax(BigDecimal.ZERO)
            .discount(BigDecimal.ZERO)
            .total(order.getTotal())
            .build();

    OrderResponse.DeliveryInfo deliveryInfo = OrderResponse.DeliveryInfo.builder()
            .address(order.getDeliveryAddress())
            .latitude(order.getLatitude() != null ? BigDecimal.valueOf(order.getLatitude()) : null)
            .longitude(order.getLongitude() != null ? BigDecimal.valueOf(order.getLongitude()) : null)
            .method(order.getDeliveryMethod() != null ? order.getDeliveryMethod().toString() : null)
            .fee(order.getDeliveryFee())
            .estimatedTimeMinutes(order.getEstimatedDeliveryTime() != null
                    ? order.getEstimatedDeliveryTime().getMinute()
                    : null)
            .build();

    return OrderResponse.builder()
            .id(order.getId())
            .orderNumber(order.getOrderNumber())
            .status(order.getStatus().toString())
            .items(itemResponses)
            .vendor(vendorInfo)
            .delivery(deliveryInfo) // ← EKLENDİ
            .totals(totals)
            .notes(order.getNotes())
            .createdAt(order.getCreatedAt())
            .updatedAt(order.getUpdatedAt())
            .estimatedDeliveryTime(order.getEstimatedDeliveryTime())
            .actualDeliveryTime(order.getDeliveredAt())
            .build();
}


    public void rateOrder(Long id, Integer rating, String comment) {
        log.info("Rating order {} with rating: {}", id, rating);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forOrder(id));

        if (order.getStatus() == null || !order.getStatus().equals(Order.OrderStatus.DELIVERED.name())) {
            throw new BadRequestException("Can only rate delivered orders");
        }

        if (rating < 1 || rating > 5) {
            throw new BadRequestException("Rating must be between 1 and 5");
        }

        // Update order rating
        order.setRating(rating);
        order.setReviewComment(comment);
        order.setReviewedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);
        log.info("Order {} rated with {} stars", id, rating);
    }

    public Object getOrderStatistics() {
        log.info("Getting order statistics");

        long totalOrders = orderRepository.count();
        long completedOrders = orderRepository.countByStatus(Order.OrderStatus.DELIVERED.name());
        long pendingOrders = orderRepository.countByStatus(Order.OrderStatus.PENDING.name());
        long cancelledOrders = orderRepository.countByStatus(Order.OrderStatus.CANCELLED.name());

        return Map.of(
            "totalOrders", totalOrders,
            "completedOrders", completedOrders,
            "pendingOrders", pendingOrders,
            "cancelledOrders", cancelledOrders,
            "completionRate", totalOrders > 0 ? (double) completedOrders / totalOrders * 100 : 0.0,
            "cancellationRate", totalOrders > 0 ? (double) cancelledOrders / totalOrders * 100 : 0.0
        );
    }

    public Object getOrderTracking(Long orderId) {
        log.info("Getting tracking information for order: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        HashMap<String, Object> trackingInfo = new HashMap<>();
        trackingInfo.put("orderId", order.getId());
        trackingInfo.put("orderNumber", order.getOrderNumber());
        trackingInfo.put("status", order.getStatus().toString());
        trackingInfo.put("currentLocation", order.getDeliveryAddress());
        trackingInfo.put("estimatedDeliveryTime", order.getEstimatedDeliveryTime());
        trackingInfo.put("actualDeliveryTime", order.getDeliveredAt());
        // If statusHistory is List<String>, just return the list
        trackingInfo.put("statusHistory", order.getStatusHistory() != null ? order.getStatusHistory() : List.of());
        return trackingInfo;
    }
}
