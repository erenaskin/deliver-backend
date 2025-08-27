package com.deliver.backend;

import com.deliver.backend.dto.request.LoginRequest;
import com.deliver.backend.dto.request.VendorRequest;
import com.deliver.backend.dto.response.LoginResponse;
import com.deliver.backend.entity.User;
import com.deliver.backend.entity.Vendor;
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
public class VendorIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private String baseUrl;
    private String userToken;
    private String vendorToken;
    private User testUser;
    private User testVendor;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
        setupTestUsers();
    }

    private void setupTestUsers() {
        // Create regular user
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
        testVendor = User.builder()
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
        testVendor = userRepository.save(testVendor);

        // Get tokens
        userToken = login("user@example.com", "Password123!");
        vendorToken = login("vendor@example.com", "Password123!");
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
    void testGetAllVendors() {
        System.out.println("User token: " + userToken);
        System.out.println("Vendor token: " + vendorToken);
        
        HttpHeaders headers = new HttpHeaders();
        if (userToken != null) {
            headers.setBearerAuth(userToken);
        }
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/api/vendors", 
                HttpMethod.GET, 
                entity, 
                String.class
        );

        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }    @Test
    void testVendorRegistration() {
        VendorRequest request = new VendorRequest();
        request.setBusinessName("Test Restaurant");
        request.setDescription("A test restaurant for testing purposes");
        request.setCategory("Restaurant");
        request.setAddress("123 Test Street");
        request.setPhoneNumber("+1234567892");
        request.setBusinessEmail("business@test.com");
        request.setBusinessLicenseNumber("LIC123456");
        request.setTaxId("TAX123456");
        request.setDeliveryFee(new BigDecimal("5.00"));
        request.setDeliveryRadiusKm(new BigDecimal("10.0"));
        request.setMinimumOrderAmount(new BigDecimal("20.00"));
        request.setEstimatedDeliveryTimeMinutes(30);
        request.setAcceptVendorTerms(true);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(userToken);
        HttpEntity<VendorRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/api/vendors/register", entity, String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void testGetVendorById() {
        // First create a vendor
        Vendor vendor = Vendor.builder()
                .user(testVendor)
                .businessName("Test Vendor")
                .description("Test Description")
                .category("RESTAURANT")
                .address("Test Address")
                .phoneNumber("+1234567893")
                .businessEmail("test@vendor.com")
                .businessLicenseNumber("LIC789")
                .taxId("TAX789")
                .deliveryFee(new BigDecimal("5.00"))
                .deliveryRadiusKm(new BigDecimal("10.0"))
                .minimumOrderAmount(new BigDecimal("20.00"))
                .estimatedDeliveryTimeMinutes(30)
                .status(Vendor.VendorStatus.ACTIVE)
                .isAcceptingOrders(true)
                .build();
        vendor = vendorRepository.save(vendor);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/api/vendors/" + vendor.getId(),
                HttpMethod.GET,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Test Vendor");
    }

    @Test
    void testGetVendorProfile() {
        // Create vendor first
        Vendor vendor = Vendor.builder()
                .user(testVendor)
                .businessName("Test Vendor Profile")
                .description("Test Description")
                .category("RESTAURANT")
                .address("Test Address")
                .phoneNumber("+1234567894")
                .businessEmail("profile@vendor.com")
                .businessLicenseNumber("LIC456")
                .taxId("TAX456")
                .deliveryFee(new BigDecimal("5.00"))
                .deliveryRadiusKm(new BigDecimal("10.0"))
                .minimumOrderAmount(new BigDecimal("20.00"))
                .estimatedDeliveryTimeMinutes(30)
                .status(Vendor.VendorStatus.ACTIVE)
                .isAcceptingOrders(true)
                .build();
        vendorRepository.save(vendor);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(vendorToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/api/vendors/profile",
                HttpMethod.GET,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Test Vendor Profile");
    }

    @Test
    void testGetVendorsWithFilters() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/api/vendors?category=RESTAURANT&search=test",
                HttpMethod.GET,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testUnauthorizedAccess() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/api/vendors/profile", String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void testGetVendorAnalytics() {
        // Create vendor first
        Vendor vendor = Vendor.builder()
                .user(testVendor)
                .businessName("Analytics Vendor")
                .description("Test Description")
                .category("RESTAURANT")
                .address("Test Address")
                .phoneNumber("+1234567895")
                .businessEmail("analytics@vendor.com")
                .businessLicenseNumber("LIC999")
                .taxId("TAX999")
                .deliveryFee(new BigDecimal("5.00"))
                .deliveryRadiusKm(new BigDecimal("10.0"))
                .minimumOrderAmount(new BigDecimal("20.00"))
                .estimatedDeliveryTimeMinutes(30)
                .status(Vendor.VendorStatus.ACTIVE)
                .isAcceptingOrders(true)
                .build();
        vendorRepository.save(vendor);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(vendorToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/api/vendors/analytics",
                HttpMethod.GET,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
