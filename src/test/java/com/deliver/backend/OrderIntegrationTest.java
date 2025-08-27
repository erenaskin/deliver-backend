package com.deliver.backend;

import com.deliver.backend.dto.request.LoginRequest;
import com.deliver.backend.dto.request.OrderRequest;
import com.deliver.backend.dto.response.LoginResponse;
import com.deliver.backend.entity.*;
import com.deliver.backend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OrderIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String baseUrl;
    private String userToken;
    private String vendorToken;
    private User testUser;
    private User vendorUser;
    private Vendor testVendor;
    private Product testProduct;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
        setupTestData();
    }

    private void setupTestData() {
        // Create test user
        testUser = User.builder()
                .email("user@example.com")
                .username("testuser")
                .firstName("Test")
                .lastName("User")
                .phoneNumber("+1234567890")
                .password(passwordEncoder.encode("Password123!"))
                .emailVerified(true)
                .status(User.UserStatus.ACTIVE)
                .roles(Set.of(User.UserRole.USER))
                .build();
        testUser = userRepository.save(testUser);

        // Create vendor user
        vendorUser = User.builder()
                .email("vendor@example.com")
                .username("testvendor")
                .firstName("Test")
                .lastName("Vendor")
                .phoneNumber("+1234567891")
                .password(passwordEncoder.encode("Password123!"))
                .emailVerified(true)
                .status(User.UserStatus.ACTIVE)
                .roles(Set.of(User.UserRole.VENDOR))
                .build();
        vendorUser = userRepository.save(vendorUser);

        // Create test vendor
        testVendor = Vendor.builder()
                .user(vendorUser)
                .businessName("Test Restaurant")
                .description("Test Description")
                .category("Restaurant")
                .address("Test Address")
                .phoneNumber("+1234567892")
                .businessEmail("test@vendor.com")
                .businessLicenseNumber("LIC123")
                .taxId("TAX123")
                .deliveryFee(new BigDecimal("5.00"))
                .deliveryRadiusKm(new BigDecimal("10.0"))
                .minimumOrderAmount(new BigDecimal("20.00"))
                .estimatedDeliveryTimeMinutes(30)
                .status(Vendor.VendorStatus.ACTIVE)
                .isAcceptingOrders(true)
                .build();
        testVendor = vendorRepository.save(testVendor);

        // Create test product
        testProduct = Product.builder()
                .vendor(testVendor)
                .name("Test Pizza")
                .description("Delicious test pizza")
                .category("Main Course")
                .subcategory("Pizza")
                .price(new BigDecimal("15.99"))
                .originalPrice(new BigDecimal("18.99"))
                .quantity(100)
                .minOrderQuantity(1)
                .maxOrderQuantity(5)
                .preparationTimeMinutes(20)
                .isAvailable(true)
                .isFeatured(false)
                .status(Product.ProductStatus.ACTIVE)
                .mainImageUrl("https://example.com/pizza.jpg")
                .build();
        testProduct = productRepository.save(testProduct);

        // Get tokens
        userToken = login("user@example.com", "Password123!");
        vendorToken = login("vendor@example.com", "Password123!");

        // Create test order
        createTestOrder();
    }

    private void createTestOrder() {
        testOrder = Order.builder()
                .orderNumber("ORDER-" + System.currentTimeMillis())
                .user(testUser)
                .vendor(testVendor)
                .status(Order.OrderStatus.PENDING)
                .deliveryAddress("123 Test Street, Test City")
                .deliveryLatitude(new BigDecimal("40.7128"))
                .deliveryLongitude(new BigDecimal("-74.0060"))
                .deliveryMethod(Order.DeliveryMethod.DELIVERY)
                .subtotal(new BigDecimal("15.99"))
                .deliveryFee(new BigDecimal("5.00"))
                .total(new BigDecimal("20.99"))
                .totalAmount(new BigDecimal("20.99"))
                .createdAt(java.time.LocalDateTime.now())
                .updatedAt(java.time.LocalDateTime.now())
                .notes("Test instructions")
                .paymentMethod(Order.PaymentMethod.CREDIT_CARD)
                .build();
        testOrder = orderRepository.save(testOrder);
    }

    private String login(String email, String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequest> entity = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
                baseUrl + "/api/auth/login", entity, LoginResponse.class
        );

        LoginResponse responseBody = response.getBody();
        return responseBody != null ? responseBody.getAccessToken() : null;
    }

    @Test
    void testCreateOrder() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setDeliveryAddress("123 Main Street, Test City");
        orderRequest.setNotes("Test order");
        orderRequest.setPaymentMethod("CREDIT_CARD");
        orderRequest.setVendorId(testVendor.getId());

        OrderRequest.OrderItemRequest itemRequest = new OrderRequest.OrderItemRequest();
        itemRequest.setProductId(testProduct.getId());
        itemRequest.setQuantity(2);

        List<OrderRequest.OrderItemRequest> items = new ArrayList<>();
        items.add(itemRequest);
        orderRequest.setItems(items);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(userToken);
        HttpEntity<OrderRequest> entity = new HttpEntity<>(orderRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/api/orders", entity, String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void testGetOrderById() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/api/orders/" + testOrder.getId(),
                HttpMethod.GET,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Test instructions");
    }

    @Test
    void testGetMyOrders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/api/orders",
                HttpMethod.GET,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testGetVendorOrders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(vendorToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/api/orders/vendor",
                HttpMethod.GET,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testUpdateOrderStatus() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(vendorToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/api/orders/" + testOrder.getId() + "/status?status=CONFIRMED",
                HttpMethod.PUT,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testCancelOrder() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/api/orders/" + testOrder.getId() + "/cancel",
                HttpMethod.PUT,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testGetOrderHistory() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/api/orders/statistics",
                HttpMethod.GET,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testGetOrderTrackingInfo() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/api/orders/" + testOrder.getId() + "/track",
                HttpMethod.GET,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testRateOrder() {
        // First, set the order status to DELIVERED so it can be rated
        Order order = orderRepository.findById(testOrder.getId()).orElseThrow();
        order.setStatus(Order.OrderStatus.DELIVERED);
        order.setDeliveredAt(LocalDateTime.now());
        orderRepository.save(order);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/api/orders/" + testOrder.getId() + "/rate?rating=5&comment=Great food!",
                HttpMethod.POST,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testGetOrderNotFound() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/api/orders/99999",
                HttpMethod.GET,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testUnauthorizedAccess() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/api/orders/my", String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void testCreateOrderWithInvalidData() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setDeliveryAddress(""); // Empty address
        orderRequest.setPaymentMethod("CREDIT_CARD");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(userToken);
        HttpEntity<OrderRequest> entity = new HttpEntity<>(orderRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/api/orders", entity, String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
