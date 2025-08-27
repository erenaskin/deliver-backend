package com.deliver.backend;

import com.deliver.backend.dto.request.LoginRequest;
import com.deliver.backend.dto.request.RegisterRequest;
import com.deliver.backend.dto.response.LoginResponse;
import com.deliver.backend.entity.User;
import com.deliver.backend.entity.EmailVerificationToken;
import com.deliver.backend.exception.BadRequestException;
import com.deliver.backend.repository.UserRepository;
import com.deliver.backend.repository.EmailVerificationTokenRepository;
import com.deliver.backend.security.JwtTokenProvider;
import com.deliver.backend.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.mail.internet.MimeMessage;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .username("testuser")
                .password("encoded_password")
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("+1234567890")
                .emailVerified(true)
                .status(User.UserStatus.ACTIVE)
                .roles(Set.of(User.UserRole.USER))
                .build();

        registerRequest = RegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .username("testuser")
                .email("test@example.com")
                .password("Password123!")
                .confirmPassword("Password123!")
                .phoneNumber("+1234567890")
                .address("123 Main Street")
                .acceptTerms(true)
                .subscribeNewsletter(false)
                .build();

        loginRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("Password123!")
                .build();
    }

    @Test
    void testRegisterSuccess() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtTokenProvider.generateTokenForUser(any(User.class))).thenReturn("jwt_token");
        when(jwtTokenProvider.generateRefreshToken(anyString())).thenReturn("refresh_token");
        when(emailVerificationTokenRepository.save(any(EmailVerificationToken.class))).thenReturn(new EmailVerificationToken());
        MimeMessage mockMimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mockMimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        // When
        LoginResponse result = authService.register(registerRequest);

        // Then
        assertNotNull(result);
        assertEquals("jwt_token", result.getAccessToken());
        assertEquals("refresh_token", result.getRefreshToken());
        assertNotNull(result.getUser());
        assertEquals("test@example.com", result.getUser().getEmail());
        
        verify(userRepository).existsByEmail("test@example.com");
        verify(userRepository).existsByUsername("testuser");
        verify(userRepository).existsByPhoneNumber("+1234567890");
        verify(userRepository).save(any(User.class));
        verify(emailVerificationTokenRepository).save(any(EmailVerificationToken.class));
        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    void testRegisterEmailAlreadyExists() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        Exception exception = assertThrows(BadRequestException.class, () -> {
            authService.register(registerRequest);
        });

        assertTrue(exception.getMessage().contains("Email address is already registered"));
        verify(userRepository).existsByEmail("test@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegisterUsernameAlreadyExists() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.register(registerRequest);
        });

        assertTrue(exception.getMessage().contains("Username already exists"));
        verify(userRepository).existsByUsername("testuser");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegisterPhoneNumberAlreadyExists() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(anyString())).thenReturn(true);

        // When & Then
        Exception exception = assertThrows(BadRequestException.class, () -> {
            authService.register(registerRequest);
        });

        assertTrue(exception.getMessage().contains("Phone number is already registered"));
        verify(userRepository).existsByPhoneNumber("+1234567890");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLoginSuccess() {
        // Given
        Authentication mockAuth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuth);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(jwtTokenProvider.generateToken(any(Authentication.class))).thenReturn("jwt_token");
        when(jwtTokenProvider.generateRefreshToken(anyString())).thenReturn("refresh_token");
        when(jwtTokenProvider.getTokenExpirationTime()).thenReturn(3600L);

        // When
        LoginResponse result = authService.login(loginRequest);

        // Then
        assertNotNull(result);
        assertEquals("jwt_token", result.getAccessToken());
        assertEquals("refresh_token", result.getRefreshToken());
        assertNotNull(result.getUser());
        assertEquals("test@example.com", result.getUser().getEmail());
        
        verify(userRepository).findByEmail("test@example.com");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testLoginUserNotFound() {
        // Given - AuthenticationManager will throw BadCredentialsException for invalid credentials
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid email or password"));

        // When & Then
        Exception exception = assertThrows(BadCredentialsException.class, () -> {
            authService.login(loginRequest);
        });

        assertTrue(exception.getMessage().contains("Invalid email or password"));
    }

    @Test
    void testLoginInvalidPassword() {
        // Given - AuthenticationManager will throw BadCredentialsException for invalid password
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid email or password"));

        // When & Then
        Exception exception = assertThrows(BadCredentialsException.class, () -> {
            authService.login(loginRequest);
        });

        assertTrue(exception.getMessage().contains("Invalid email or password"));
    }

    @Test
    void testLoginInactiveUser() {
        // Given - AuthenticationManager will throw BadCredentialsException for inactive user
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid email or password"));

        // When & Then
        Exception exception = assertThrows(BadCredentialsException.class, () -> {
            authService.login(loginRequest);
        });

        assertTrue(exception.getMessage().contains("Invalid email or password"));
    }
}
