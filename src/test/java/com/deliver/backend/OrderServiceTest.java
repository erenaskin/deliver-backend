package com.deliver.backend;

import com.deliver.backend.dto.request.OrderRequest;
import com.deliver.backend.dto.response.OrderResponse;
import com.deliver.backend.entity.Order;
import com.deliver.backend.entity.User;
import com.deliver.backend.entity.Vendor;
import com.deliver.backend.repository.OrderRepository;
import com.deliver.backend.repository.UserRepository;
import com.deliver.backend.repository.VendorRepository;
import com.deliver.backend.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VendorRepository vendorRepository;

    @InjectMocks
    private OrderService orderService;

    private Order testOrder;
    private User testUser;
    private Vendor testVendor;
    private OrderRequest testOrderRequest;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .build();

        testVendor = Vendor.builder()
                .id(1L)
                .businessName("Test Restaurant")
                .phoneNumber("+1234567890")
                .deliveryFee(new BigDecimal("2.99"))
                .build();

        testOrder = Order.builder()
                .id(1L)
                .user(testUser)
                .vendor(testVendor)
                .subtotal(new BigDecimal("25.98"))
                .deliveryFee(new BigDecimal("2.99"))
                .total(new BigDecimal("28.97"))
                .status(Order.OrderStatus.PENDING)
                .deliveryAddress("123 Test St")
                .orderNumber("ORD-2023-001")
                .createdAt(LocalDateTime.now())
                .build();

        testOrderRequest = OrderRequest.builder()
                .deliveryAddress("123 Test St")
                .notes("Test order")
                .paymentMethod("CREDIT_CARD") // zorunlu alanı ekledik
                .items(List.of(
                        new OrderRequest.OrderItemRequest(1L, 2, null) // productId, quantity, specialInstructions
                ))
                .build();
    }

    @Test
    void testGetUserOrders() {
        List<Order> orders = Arrays.asList(testOrder);
        when(orderRepository.findByUserId(1L)).thenReturn(orders);

        List<OrderResponse> result = orderService.getUserOrders(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository).findByUserId(1L);
    }

    @Test
    void testGetOrderById() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        OrderResponse result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals("ORD-2023-001", result.getOrderNumber());
        assertEquals(new BigDecimal("28.97"), result.getTotals().getTotal());
        verify(orderRepository).findById(1L);
    }

    @Test
    void testCreateOrder() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(vendorRepository.findById(1L)).thenReturn(Optional.of(testVendor));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        OrderResponse result = orderService.createOrder(testOrderRequest, 1L, 1L);

        assertNotNull(result);
        assertEquals("123 Test St", result.getDelivery().getAddress());
        verify(userRepository).findById(1L);
        verify(vendorRepository).findById(1L);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testUpdateOrderStatus() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        OrderResponse result = orderService.updateOrderStatus(1L, Order.OrderStatus.CONFIRMED);

        assertNotNull(result);
        verify(orderRepository).findById(1L);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testCancelOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        orderService.cancelOrder(1L, "Customer request");

        verify(orderRepository).findById(1L);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testGetOrderStatistics() {
    when(orderRepository.count()).thenReturn(100L);
    when(orderRepository.countByStatus("DELIVERED")).thenReturn(80L);
    when(orderRepository.countByStatus("PENDING")).thenReturn(15L);
    when(orderRepository.countByStatus("CANCELLED")).thenReturn(5L);

    Object result = orderService.getOrderStatistics();

    assertNotNull(result);
    verify(orderRepository).count();
    verify(orderRepository, times(3)).countByStatus(any(String.class));
    }
}
