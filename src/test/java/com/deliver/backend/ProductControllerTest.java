package com.deliver.backend;

import com.deliver.backend.entity.Product;
import com.deliver.backend.entity.User;
import com.deliver.backend.entity.Vendor;
import com.deliver.backend.repository.ProductRepository;
import com.deliver.backend.repository.UserRepository;
import com.deliver.backend.repository.VendorRepository;
import com.deliver.backend.security.JwtTokenProvider;
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
public class ProductControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String baseUrl;
    private String jwtToken;
    private User testUser;
    private Vendor testVendor;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;

        // Test user oluştur ve VENDOR rolü ata
        testUser = User.builder()
                .email("test@example.com")
                .username("testuser")
                .password(passwordEncoder.encode("password123"))
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("+1234567890")
                .emailVerified(true)
                .roles(Set.of(User.UserRole.VENDOR))
                .build();
        testUser = userRepository.save(testUser);

        // JWT token oluştur
        jwtToken = jwtTokenProvider.generateToken(testUser.getEmail());

        // Test vendor oluştur
        testVendor = Vendor.builder()
                .user(testUser)
                .businessName("Test Restaurant")
                .description("Test restaurant description")
                .category("Restaurant")
                .address("123 Test Street")
                .phoneNumber("+1234567890")
                .deliveryFee(new BigDecimal("2.99"))
                .deliveryRadiusKm(new BigDecimal("5.0"))
                .estimatedDeliveryTimeMinutes(30)
                .minimumOrderAmount(new BigDecimal("10.00"))
                .status(Vendor.VendorStatus.ACTIVE)
                .taxId("TAX123456")
                .businessLicenseNumber("BLN123456")
                .build();
        testVendor = vendorRepository.save(testVendor);

        // Test product oluştur
        testProduct = Product.builder()
                .vendor(testVendor)
                .name("Test Pizza")
                .description("Delicious test pizza")
                .category("Pizza")
                .price(new BigDecimal("15.99"))
                .quantity(50)
                .isAvailable(true)
                .isFeatured(false)
                .status(Product.ProductStatus.ACTIVE)
                .build();
        testProduct = productRepository.save(testProduct);
    }

    @Test
    void testGetAllProductsWithAuth() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // ProductController returns paginated response, not array
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/api/products", HttpMethod.GET, entity, String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("content");
        assertThat(response.getBody()).contains("Test Pizza");
    }

    @Test
    void testGetAllProductsWithoutAuth() {
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/api/products", HttpMethod.GET, HttpEntity.EMPTY, String.class
        );

        // Spring Security returns 401 UNAUTHORIZED for unauthenticated requests
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void testCreateProductWithAuth() {
        // Create product request as JSON string to avoid serialization issues
        String productRequestJson = """
            {
                "name": "New Pizza",
                "description": "Brand new pizza",
                "category": "Pizza",
                "price": 18.99,
                "quantity": 50,
                "minOrderQuantity": 1
            }
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(productRequestJson, headers);

        // Use VendorController endpoint which returns String response
        ResponseEntity<String> response = restTemplate.postForEntity(
            baseUrl + "/api/vendors/products", entity, String.class
        );

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testCreateProductWithoutAuth() {
        String productRequestJson = """
            {
                "name": "Unauthorized Pizza",
                "description": "This should fail",
                "category": "Pizza",
                "price": 20.99,
                "quantity": 50,
                "minOrderQuantity": 1
            }
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(productRequestJson, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
            baseUrl + "/api/vendors/products", entity, String.class
        );
        
        // Spring Security returns 401 UNAUTHORIZED for unauthenticated requests
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
