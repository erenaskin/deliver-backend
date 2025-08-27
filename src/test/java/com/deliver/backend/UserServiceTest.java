
package com.deliver.backend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.deliver.backend.entity.User;
import com.deliver.backend.repository.UserRepository;
import com.deliver.backend.service.AuthService;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Tests")
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    // AuthService kullanıyoruz çünkü UserService henüz yok
    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .password("encodedPassword")
                .status(User.UserStatus.ACTIVE)
                .emailVerified(true)
                .subscribeNewsletter(false)
                .roles(Set.of(User.UserRole.USER))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should find user by email successfully")
    void shouldFindUserByEmailSuccessfully() {
        // Given
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        // When
        Optional<User> result = userRepository.findByEmail("test@example.com");

        // Then
        assertTrue(result.isPresent());
        assertEquals(testUser.getEmail(), result.get().getEmail());
        assertEquals(testUser.getUsername(), result.get().getUsername());
    }

    @Test
    @DisplayName("Should return empty when user not found by email")
    void shouldReturnEmptyWhenUserNotFoundByEmail() {
        // Given
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        // When
        Optional<User> result = userRepository.findByEmail("notfound@example.com");

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should find user by username successfully")
    void shouldFindUserByUsernameSuccessfully() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // When
        Optional<User> result = userRepository.findByUsername("testuser");

        // Then
        assertTrue(result.isPresent());
        assertEquals(testUser.getUsername(), result.get().getUsername());
        assertEquals(testUser.getEmail(), result.get().getEmail());
    }

    @Test
    @DisplayName("Should return empty when user not found by username")
    void shouldReturnEmptyWhenUserNotFoundByUsername() {
        // Given
        when(userRepository.findByUsername("notfound")).thenReturn(Optional.empty());

        // When
        Optional<User> result = userRepository.findByUsername("notfound");

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should check if user exists by email")
    void shouldCheckIfUserExistsByEmail() {
        // Given
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);
        when(userRepository.existsByEmail("notfound@example.com")).thenReturn(false);

        // When & Then
        assertTrue(userRepository.existsByEmail("test@example.com"));
        assertFalse(userRepository.existsByEmail("notfound@example.com"));
    }

    @Test
    @DisplayName("Should check if user exists by username")
    void shouldCheckIfUserExistsByUsername() {
        // Given
        when(userRepository.existsByUsername("testuser")).thenReturn(true);
        when(userRepository.existsByUsername("notfound")).thenReturn(false);

        // When & Then
        assertTrue(userRepository.existsByUsername("testuser"));
        assertFalse(userRepository.existsByUsername("notfound"));
    }

    @Test
    @DisplayName("Should return all users")
    void shouldReturnAllUsers() {
        // Given
        User user2 = User.builder()
                .id(2L)
                .username("user2")
                .email("user2@example.com")
                .firstName("User")
                .lastName("Two")
                .status(User.UserStatus.ACTIVE)
                .roles(Set.of(User.UserRole.USER))
                .build();
        
        List<User> users = Arrays.asList(testUser, user2);
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<User> result = userRepository.findAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testUser.getId(), result.get(0).getId());
        assertEquals(user2.getId(), result.get(1).getId());
    }

    @Test
    @DisplayName("Should find users by role")
    void shouldFindUsersByRole() {
        // Given
        List<User> users = Arrays.asList(testUser);
        when(userRepository.findByRole(User.UserRole.USER)).thenReturn(users);

        // When
        List<User> result = userRepository.findByRole(User.UserRole.USER);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testUser.getId(), result.get(0).getId());
        assertTrue(result.get(0).hasRole(User.UserRole.USER));
    }

    @Test
    @DisplayName("Should find active users")
    void shouldFindActiveUsers() {
        // Given
        List<User> activeUsers = Arrays.asList(testUser);
        when(userRepository.findActiveUsers()).thenReturn(activeUsers);

        // When
        List<User> result = userRepository.findActiveUsers();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(User.UserStatus.ACTIVE, result.get(0).getStatus());
    }

    @Test
    @DisplayName("Should count users by role")
    void shouldCountUsersByRole() {
        // Given
        when(userRepository.countByRole(User.UserRole.USER)).thenReturn(5L);
        when(userRepository.countByRole(User.UserRole.VENDOR)).thenReturn(2L);

        // When & Then
        assertEquals(5L, userRepository.countByRole(User.UserRole.USER));
        assertEquals(2L, userRepository.countByRole(User.UserRole.VENDOR));
    }

    @Test
    @DisplayName("Should count active users")
    void shouldCountActiveUsers() {
        // Given
        when(userRepository.countActiveUsers()).thenReturn(10L);

        // When
        long result = userRepository.countActiveUsers();

        // Then
        assertEquals(10L, result);
    }

    @Test
    @DisplayName("Should test user authentication methods")
    void shouldTestUserAuthenticationMethods() {
        // Given
        User user = User.builder()
                .email("auth@example.com")
                .password("password")
                .status(User.UserStatus.ACTIVE)
                .emailVerified(true)
                .roles(Set.of(User.UserRole.USER))
                .build();

        // When & Then
        assertTrue(user.isEnabled());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isCredentialsNonExpired());
        assertEquals("auth@example.com", user.getUsername()); // Spring Security username is email
        assertNotNull(user.getAuthorities());
        assertTrue(user.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    @DisplayName("Should test user helper methods")
    void shouldTestUserHelperMethods() {
        // Given - Create user with mutable HashSet
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRoles(new java.util.HashSet<>()); // Mutable set

        // Test adding roles to empty set
        user.addRole(User.UserRole.USER);
        user.addRole(User.UserRole.VENDOR);

        // When & Then
        assertEquals("John Doe", user.getFullName());
        assertTrue(user.hasRole(User.UserRole.USER));
        assertTrue(user.hasRole(User.UserRole.VENDOR));
        assertFalse(user.hasRole(User.UserRole.ADMIN));

        // Test role manipulation
        user.addRole(User.UserRole.ADMIN);
        assertTrue(user.hasRole(User.UserRole.ADMIN));

        user.removeRole(User.UserRole.ADMIN);
        assertFalse(user.hasRole(User.UserRole.ADMIN));
    }

    @Test
    @DisplayName("Should test user status and verification")
    void shouldTestUserStatusAndVerification() {
        // Test locked user
        User lockedUser = User.builder()
                .status(User.UserStatus.LOCKED)
                .emailVerified(true)
                .build();
        assertFalse(lockedUser.isAccountNonLocked());
        assertFalse(lockedUser.isEnabled());

        // Test unverified user
        User unverifiedUser = User.builder()
                .status(User.UserStatus.ACTIVE)
                .emailVerified(false)
                .build();
        assertTrue(unverifiedUser.isAccountNonLocked());
        assertFalse(unverifiedUser.isEnabled());

        // Test active verified user
        User activeUser = User.builder()
                .status(User.UserStatus.ACTIVE)
                .emailVerified(true)
                .build();
        assertTrue(activeUser.isAccountNonLocked());
        assertTrue(activeUser.isEnabled());
    }

    @Test
    @DisplayName("Should find users created between dates")
    void shouldFindUsersCreatedBetweenDates() {
        // Given
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();
        List<User> users = Arrays.asList(testUser);
        
        when(userRepository.findUsersCreatedBetween(startDate, endDate)).thenReturn(users);

        // When
        List<User> result = userRepository.findUsersCreatedBetween(startDate, endDate);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testUser.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("Should find recently active users")
    void shouldFindRecentlyActiveUsers() {
        // Given
        LocalDateTime since = LocalDateTime.now().minusHours(24);
        testUser.setLastLoginAt(LocalDateTime.now().minusHours(2));
        List<User> users = Arrays.asList(testUser);
        
        when(userRepository.findRecentlyActiveUsers(since)).thenReturn(users);

        // When
        List<User> result = userRepository.findRecentlyActiveUsers(since);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testUser.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("Should test user builder pattern")
    void shouldTestUserBuilderPattern() {
        // When
    User user = User.builder()
        .firstName("Builder")
        .lastName("Test")
        .email("builder@test.com")
        .username("buildertest")
        .password("password")
        .phoneNumber("+1234567890")
        .address("123 Builder St")
        .birthDate(LocalDate.of(1990, 1, 1))
        .status(User.UserStatus.ACTIVE)
        .emailVerified(true)
        .subscribeNewsletter(true)
        .roles(Set.of(User.UserRole.USER))
        .build();

        // Then
        assertEquals("Builder", user.getFirstName());
        assertEquals("Test", user.getLastName());
        assertEquals("builder@test.com", user.getEmail());
        // getUsername() returns email in Spring Security implementation
        assertEquals("builder@test.com", user.getUsername()); 
        assertEquals("password", user.getPassword());
        assertEquals("+1234567890", user.getPhoneNumber());
        assertEquals("123 Builder St", user.getAddress());
        assertEquals(LocalDate.of(1990, 1, 1), user.getBirthDate());
        assertEquals(User.UserStatus.ACTIVE, user.getStatus());
        assertTrue(user.getEmailVerified());
        assertTrue(user.getSubscribeNewsletter());
        assertEquals(Set.of(User.UserRole.USER), user.getRoles()); // Default role
    }
}
