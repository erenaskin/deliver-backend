package com.deliver.backend;

import com.deliver.backend.dto.request.LoginRequest;
import com.deliver.backend.dto.response.LoginResponse;
import com.deliver.backend.entity.Product;
import com.deliver.backend.entity.User;
import com.deliver.backend.entity.Vendor;
import com.deliver.backend.repository.ProductRepository;
import com.deliver.backend.repository.UserRepository;
import com.deliver.backend.repository.VendorRepository;
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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductIntegrationTest {

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
    private PasswordEncoder passwordEncoder;

    private String baseUrl;
    private String userToken;
    private User testUser;
    private Vendor testVendor;
    private Product testProduct;

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

        // Create test vendor
        User vendorUser = User.builder()
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

        // Get user token
        userToken = login("user@example.com", "Password123!");
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
    void testGetAllProducts() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/api/products", 
                HttpMethod.GET, 
                entity, 
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testGetProductById() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/api/products/" + testProduct.getId(),
                HttpMethod.GET,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Test Pizza");
    }

    @Test
    void testGetProductsByVendor() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/api/products/vendor/" + testVendor.getId(),
                HttpMethod.GET,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Test Pizza");
    }

    @Test
    void testGetProductsWithFilters() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/api/products?category=Main Course&search=pizza&minPrice=10&maxPrice=20",
                HttpMethod.GET,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testGetProductCategories() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/api/products/categories",
                HttpMethod.GET,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testGetFeaturedProducts() {
        // First make the product featured
        testProduct.setIsFeatured(true);
        productRepository.save(testProduct);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/api/products/featured",
                HttpMethod.GET,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testSearchProducts() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/api/products/search?query=pizza",
                HttpMethod.GET,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testAddProductToFavorites() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/api/products/" + testProduct.getId() + "/favorite",
                HttpMethod.POST,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testRemoveProductFromFavorites() {
        // First add to favorites
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        restTemplate.exchange(
                baseUrl + "/api/products/" + testProduct.getId() + "/favorite",
                HttpMethod.POST,
                entity,
                String.class
        );

        // Then remove from favorites
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/api/products/" + testProduct.getId() + "/favorite",
                HttpMethod.DELETE,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testGetFavoriteProducts() {
        // First add product to favorites
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        restTemplate.exchange(
                baseUrl + "/api/products/" + testProduct.getId() + "/favorite",
                HttpMethod.POST,
                entity,
                String.class
        );

        // Then get favorites
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/api/products/favorites",
                HttpMethod.GET,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testGetProductNotFound() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/api/products/99999",
                HttpMethod.GET,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testUnauthorizedAccess() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/api/products", String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
