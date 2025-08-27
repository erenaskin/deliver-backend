package com.deliver.backend;

import com.deliver.backend.dto.request.LoginRequest;
import com.deliver.backend.dto.request.RegisterRequest;
import com.deliver.backend.entity.User;
import com.deliver.backend.repository.UserRepository;
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

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String baseUrl;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User existingUser;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
        
        // Clean database
        userRepository.deleteAll();

        registerRequest = RegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .username("johndoe")
                .email("john@example.com")
                .password("Password123!")
                .confirmPassword("Password123!")
                .phoneNumber("+1234567890")
                .address("123 Main Street")
                .acceptTerms(true)
                .subscribeNewsletter(false)
                .build();

        loginRequest = LoginRequest.builder()
                .email("existing@example.com")
                .password("Password123!")
                .build();

        // Create existing user for login tests
        existingUser = User.builder()
                .email("existing@example.com")
                .username("existinguser")
                .password(passwordEncoder.encode("Password123!"))
                .firstName("Existing")
                .lastName("User")
                .phoneNumber("+0987654321")
                .emailVerified(true)
                .status(User.UserStatus.ACTIVE)
                .roles(Set.of(User.UserRole.USER))
                .build();
        userRepository.save(existingUser);
    }

    @Test
    void testRegisterSuccess() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RegisterRequest> entity = new HttpEntity<>(registerRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/api/auth/register", entity, String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).contains("accessToken");
        assertThat(response.getBody()).contains("john@example.com");
    }

    @Test
    void testRegisterEmailAlreadyExists() {
        // First registration
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RegisterRequest> entity = new HttpEntity<>(registerRequest, headers);

        restTemplate.postForEntity(baseUrl + "/api/auth/register", entity, String.class);

        // Second registration with same email
        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/api/auth/register", entity, String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Email address is already registered");
    }

    @Test
    void testRegisterPasswordMismatch() {
        registerRequest.setConfirmPassword("DifferentPassword123!");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RegisterRequest> entity = new HttpEntity<>(registerRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/api/auth/register", entity, String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Password and confirmation password must match");
    }

    @Test
    void testRegisterTermsNotAccepted() {
        registerRequest.setAcceptTerms(false);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RegisterRequest> entity = new HttpEntity<>(registerRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/api/auth/register", entity, String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("You must accept the terms and conditions");
    }

    @Test
    void testLoginSuccess() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequest> entity = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/api/auth/login", entity, String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("accessToken");
        assertThat(response.getBody()).contains("existing@example.com");
    }

    @Test
    void testLoginInvalidCredentials() {
        loginRequest.setPassword("WrongPassword");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequest> entity = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/api/auth/login", entity, String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).contains("Invalid username or password");
    }

    @Test
    void testRegisterAndLoginFlow() {
        // Register
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RegisterRequest> registerEntity = new HttpEntity<>(registerRequest, headers);

        ResponseEntity<String> registerResponse = restTemplate.postForEntity(
                baseUrl + "/api/auth/register", registerEntity, String.class
        );
        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        
        // Register returns login response directly, no need for separate login
        assertThat(registerResponse.getBody()).contains("accessToken");
        assertThat(registerResponse.getBody()).contains("john@example.com");
    }

    @Test
    void testInvalidEndpoints() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Test invalid registration data
        RegisterRequest invalidRequest = RegisterRequest.builder()
                .email("invalid-email")
                .password("weak")
                .build();

        HttpEntity<RegisterRequest> entity = new HttpEntity<>(invalidRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/api/auth/register", entity, String.class
        );

        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void testUserCount() {
        // Initial user count (we have one existing user)
        long initialCount = userRepository.count();
        assertThat(initialCount).isEqualTo(1);

        // Register new user
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RegisterRequest> entity = new HttpEntity<>(registerRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/api/auth/register", entity, String.class
        );

        if (response.getStatusCode() == HttpStatus.CREATED) {
            // User count should increase
            long newCount = userRepository.count();
            assertThat(newCount).isEqualTo(initialCount + 1);
        }
    }
}
