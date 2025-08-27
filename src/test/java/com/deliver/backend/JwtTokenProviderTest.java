package com.deliver.backend;

import com.deliver.backend.entity.User;
import com.deliver.backend.security.JwtTokenProvider;
import com.deliver.backend.config.JwtConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class JwtTokenProviderTest {

    @Mock
    private JwtConfig jwtConfig;

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .username("testuser")
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("+1234567890")
                .password("encoded_password")
                .emailVerified(true)
                .status(User.UserStatus.ACTIVE)
                .roles(Set.of(User.UserRole.USER))
                .build();
        
        // Mock JwtConfig values - HS512 requires at least 512 bits (64 characters)
        String validSecret = "mySecretKeyThatIsAtLeast512BitsLongForHS512AlgorithmTestingPurposeOnly123456789012345678901234567890";
        lenient().when(jwtConfig.getSecret()).thenReturn(validSecret);
        lenient().when(jwtConfig.getExpiration()).thenReturn(86400000L); // 24 hours
        lenient().when(jwtConfig.getRefreshExpiration()).thenReturn(604800000L); // 7 days
        lenient().when(jwtConfig.getIssuer()).thenReturn("deliver-backend");
    }

    @Test
    void testGenerateTokenForUser() {
        // When
        String token = jwtTokenProvider.generateTokenForUser(testUser);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.length() > 50); // JWT token uzun olmalı
    }

    @Test
    void testGenerateToken() {
        // When
        String token = jwtTokenProvider.generateToken("test@example.com");

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.length() > 50);
    }

    @Test
    void testGenerateRefreshToken() {
        // When
        String refreshToken = jwtTokenProvider.generateRefreshToken("test@example.com");

        // Then
        assertNotNull(refreshToken);
        assertFalse(refreshToken.isEmpty());
        assertTrue(refreshToken.length() > 50);
    }

    @Test
    void testGetUsernameFromToken() {
        // Given
        String token = jwtTokenProvider.generateToken("test@example.com");

        // When
        String extractedUsername = jwtTokenProvider.getUsernameFromToken(token);

        // Then
        assertEquals("test@example.com", extractedUsername);
    }

    @Test
    void testValidateToken() {
        // Given
        String token = jwtTokenProvider.generateToken("test@example.com");

        // When
        boolean isValid = jwtTokenProvider.validateToken(token);

        // Then
        assertTrue(isValid);
    }

    @Test
    void testIsTokenExpired() {
        // Given
        String token = jwtTokenProvider.generateToken("test@example.com");

        // When
        boolean isExpired = jwtTokenProvider.isTokenExpired(token);

        // Then
        assertFalse(isExpired); // Yeni token expire olmamış olmalı
    }

    @Test
    void testInvalidToken() {
        // Given
        String invalidToken = "invalid.token.here";

        // When
        boolean isValid = jwtTokenProvider.validateToken(invalidToken);
        String username = jwtTokenProvider.getUsernameFromToken(invalidToken);

        // Then
        assertFalse(isValid);
        assertNull(username);
    }

    @Test
    void testGetRolesFromToken() {
        // Given
        String token = jwtTokenProvider.generateTokenForUser(testUser);

        // When
        String roles = jwtTokenProvider.getRolesFromToken(token);

        // Then
        assertNotNull(roles);
        assertTrue(roles.contains("USER"));
    }

    @Test
    void testGetExpirationDateFromToken() {
        // Given
        String token = jwtTokenProvider.generateToken("test@example.com");

        // When
        java.util.Date expirationDate = jwtTokenProvider.getExpirationDateFromToken(token);

        // Then
        assertNotNull(expirationDate);
        assertTrue(expirationDate.after(new java.util.Date()));
    }

    @Test
    void testCanTokenBeRefreshed() {
        // Given
        String token = jwtTokenProvider.generateToken("test@example.com");

        // When
        boolean canBeRefreshed = jwtTokenProvider.canTokenBeRefreshed(token);

        // Then
        assertTrue(canBeRefreshed);
    }

    @Test
    void testTokenParsing() {
        // Given
        String email = "test@example.com";
        String token = jwtTokenProvider.generateToken(email);

        // When
        String extractedEmail = jwtTokenProvider.getUsernameFromToken(token);
        boolean isExpired = jwtTokenProvider.isTokenExpired(token);

        // Then
        assertEquals(email, extractedEmail);
        assertFalse(isExpired);
    }

    @Test
    void testRefreshTokenGeneration() {
        // Given
        String email = "test@example.com";

        // When
        String accessToken = jwtTokenProvider.generateToken(email);
        String refreshToken = jwtTokenProvider.generateRefreshToken(email);

        // Then
        assertNotNull(accessToken);
        assertNotNull(refreshToken);
        assertNotEquals(accessToken, refreshToken);
        
        // Her iki token'dan da email'i çıkarabilmeliyiz
        assertEquals(email, jwtTokenProvider.getUsernameFromToken(accessToken));
        assertEquals(email, jwtTokenProvider.getUsernameFromToken(refreshToken));
    }

    @Test
    void testTokenValidation() {
        // Given
        String validToken = jwtTokenProvider.generateToken("test@example.com");
        String invalidToken = "invalid.token.here";

        // When & Then
        assertTrue(jwtTokenProvider.validateToken(validToken));
        assertFalse(jwtTokenProvider.validateToken(invalidToken));
        assertFalse(jwtTokenProvider.validateToken(null));
        assertFalse(jwtTokenProvider.validateToken(""));
    }

    @Test
    void testTokenClaims() {
        // Given
        String token = jwtTokenProvider.generateTokenForUser(testUser);

        // When
        var claims = jwtTokenProvider.getClaimsFromToken(token);

        // Then
        assertNotNull(claims);
        assertEquals("test@example.com", claims.getSubject());
        assertNotNull(claims.get("roles"));
        assertEquals("deliver-backend", claims.getIssuer());
    }

    @Test
    void testPasswordResetToken() {
        // Given
        String email = "test@example.com";

        // When
        String resetToken = jwtTokenProvider.generatePasswordResetToken(email);

        // Then
        assertNotNull(resetToken);
        assertFalse(resetToken.isEmpty());
        
        // Validate the token
        String extractedEmail = jwtTokenProvider.validatePasswordResetToken(resetToken);
        assertEquals(email, extractedEmail);
    }

    @Test
    void testEmailVerificationToken() {
        // Given
        String email = "test@example.com";

        // When
        String verificationToken = jwtTokenProvider.generateEmailVerificationToken(email);

        // Then
        assertNotNull(verificationToken);
        assertFalse(verificationToken.isEmpty());
        
        // Validate the token
        String extractedEmail = jwtTokenProvider.validateEmailVerificationToken(verificationToken);
        assertEquals(email, extractedEmail);
    }

    @Test
    void testInvalidPasswordResetToken() {
        // Given
        String normalToken = jwtTokenProvider.generateToken("test@example.com");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            jwtTokenProvider.validatePasswordResetToken(normalToken);
        });
    }

    @Test
    void testInvalidEmailVerificationToken() {
        // Given
        String normalToken = jwtTokenProvider.generateToken("test@example.com");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            jwtTokenProvider.validateEmailVerificationToken(normalToken);
        });
    }

    @Test
    void testTokenExpirationTimes() {
        // When
        long accessTokenExpiration = jwtTokenProvider.getTokenExpirationTime();
        long refreshTokenExpiration = jwtTokenProvider.getRefreshTokenExpirationTime();

        // Then
        assertEquals(86400000L, accessTokenExpiration); // 24 hours
        assertEquals(604800000L, refreshTokenExpiration); // 7 days
    }
}
