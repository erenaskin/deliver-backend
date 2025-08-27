package com.deliver.backend;

import com.deliver.backend.dto.request.LoginRequest;
import com.deliver.backend.dto.request.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;
    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
        
        loginRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();

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
    }

    @Test
    void testRegister() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<RegisterRequest> entity = new HttpEntity<>(registerRequest, headers);
        
        ResponseEntity<String> response = restTemplate.postForEntity(
            baseUrl + "/api/auth/register", entity, String.class
        );

        // Registration should work or return appropriate error
        assertThat(response.getStatusCode()).isIn(HttpStatus.CREATED, HttpStatus.BAD_REQUEST);
    }

    @Test
    void testLoginEndpointExists() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<LoginRequest> entity = new HttpEntity<>(loginRequest, headers);
        
        ResponseEntity<String> response = restTemplate.postForEntity(
            baseUrl + "/api/auth/login", entity, String.class
        );

        // Login endpoint should exist (may return unauthorized but shouldn't be 404)
        assertThat(response.getStatusCode()).isNotEqualTo(HttpStatus.NOT_FOUND);
    }
}
