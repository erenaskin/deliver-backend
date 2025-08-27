package com.deliver.backend.service;

import com.deliver.backend.dto.request.LoginRequest;
import com.deliver.backend.dto.request.RegisterRequest;
import com.deliver.backend.dto.response.LoginResponse;
import com.deliver.backend.entity.EmailVerificationToken;
import com.deliver.backend.entity.User;
import com.deliver.backend.exception.BadRequestException;
import com.deliver.backend.exception.ResourceNotFoundException;
import com.deliver.backend.repository.EmailVerificationTokenRepository;
import com.deliver.backend.repository.UserRepository;
import com.deliver.backend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final JavaMailSender mailSender;

    private static final Pattern EMAIL_PATTERN = 
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    private static final Pattern PHONE_PATTERN = 
            Pattern.compile("^\\+?[1-9]\\d{1,14}$");

    @Transactional
    public LoginResponse login(LoginRequest request) {
        log.info("Attempting login for user: {}", request.getEmail());
        
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // Generate tokens
            String accessToken = jwtTokenProvider.generateToken(authentication);
            String refreshToken = jwtTokenProvider.generateRefreshToken(request.getEmail());

            // Get user details
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> ResourceNotFoundException.forUser(request.getEmail()));

            // Email doğrulanmadıysa girişe izin verme
            if (!user.getEmailVerified()) {
                log.warn("Login denied for {}: email not verified.", request.getEmail());
                throw new BadCredentialsException("Email adresiniz doğrulanmadı.");
            }

            // Update last login
            user.setLastLoginAt(LocalDateTime.now());
            userRepository.save(user);

            log.info("User {} logged in successfully", request.getEmail());

            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(jwtTokenProvider.getTokenExpirationTime())
                    .user(mapToUserInfo(user))
                    .build();

        } catch (AuthenticationException e) {
            log.error("Authentication failed for user: {}", request.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    @Transactional
    public LoginResponse register(RegisterRequest request) {
        log.info("Attempting registration for user: {}", request.getEmail());
        log.debug("Request details - Email: {}, Username: {}, Password length: {}",
                request.getEmail(), request.getUsername(),
                request.getPassword() != null ? request.getPassword().length() : "null");

        // Validate input
        validateRegistrationRequest(request);


        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw BadRequestException.emailAlreadyExists();
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists");
        }

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw BadRequestException.phoneAlreadyExists();
        }

        // Create new user
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(User.UserRole.USER))
                .status(User.UserStatus.ACTIVE)
                .emailVerified(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);
        log.info("User {} registered successfully", request.getEmail());
        // Otomatik olarak email doğrulama maili gönder
        sendVerificationEmail(savedUser);

        // Generate tokens directly without authentication
        String accessToken = jwtTokenProvider.generateTokenForUser(savedUser);
        String refreshToken = jwtTokenProvider.generateRefreshToken(savedUser.getEmail());

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(3600L)
                .user(LoginResponse.UserInfo.builder()
                        .id(savedUser.getId())
                        .username(savedUser.getUsername())
                        .firstName(savedUser.getFirstName())
                        .lastName(savedUser.getLastName())
                        .email(savedUser.getEmail())
                        .roles(savedUser.getRoles().stream()
                                .map(Enum::name)
                                .collect(java.util.stream.Collectors.toSet()))
                        .emailVerified(savedUser.getEmailVerified())
                        .status(savedUser.getStatus().name())
                        .profilePictureUrl(savedUser.getProfilePictureUrl())
                        .lastLoginAt(savedUser.getLastLoginAt())
                        .createdAt(savedUser.getCreatedAt())
                        .build())
                .build();
    }

    @Transactional
    public LoginResponse refreshToken(String refreshToken) {
        log.info("Attempting token refresh");
        
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new BadRequestException("Invalid refresh token");
        }
        
        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> ResourceNotFoundException.forUser(username));
        
        // Generate new tokens
        String newAccessToken = jwtTokenProvider.generateTokenForUser(user);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(username);
        
        log.info("Token refreshed successfully for user: {}", username);
        
        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getTokenExpirationTime())
                .user(mapToUserInfo(user))
                .build();
    }

    @Transactional
    public void logout(String username) {
        log.info("User {} logged out", username);
        // In a real application, you might want to:
        // 1. Blacklist the JWT token
        // 2. Clear any cached sessions
        // 3. Update last activity timestamp
        
        userRepository.findByEmail(username)
                .ifPresent(user -> {
                    // Could add logout timestamp or other logout logic here
                    log.debug("Logout processed for user: {}", username);
                });
    }

    @Transactional(readOnly = true)
    public User getCurrentUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> ResourceNotFoundException.forUser(email));
    }

    private void validateRegistrationRequest(RegisterRequest request) {
        // Validate email format
        if (!EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
            throw BadRequestException.invalidEmail();
        }
        
        // Validate phone format
        if (!PHONE_PATTERN.matcher(request.getPhoneNumber()).matches()) {
            throw new BadRequestException("Invalid phone number format");
        }
        
        // Validate password strength
        if (!isPasswordStrong(request.getPassword())) {
            throw BadRequestException.weakPassword();
        }
        
        // Validate required fields
        if (request.getFirstName() == null || request.getFirstName().trim().isEmpty()) {
            throw new BadRequestException("First name is required");
        }
        
        if (request.getLastName() == null || request.getLastName().trim().isEmpty()) {
            throw new BadRequestException("Last name is required");
        }
    }

    private boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.chars().anyMatch(c -> "!@#$%^&*()_+-=[]{}|;:,.<>?".indexOf(c) >= 0);
        
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    private LoginResponse.UserInfo mapToUserInfo(User user) {
        return LoginResponse.UserInfo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(Enum::name).collect(java.util.stream.Collectors.toSet()))
                .emailVerified(user.getEmailVerified())
                .status(user.getStatus().name())
                .profilePictureUrl(user.getProfilePictureUrl())
                .lastLoginAt(user.getLastLoginAt())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public void forgotPassword(String email) {
        log.info("Processing forgot password request for email: {}", email);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        
        // Generate password reset token (valid for 1 hour)
        String resetToken = jwtTokenProvider.generatePasswordResetToken(user.getEmail());
        
        // In a real application, you would:
        // 1. Store the token in database with expiration
        // 2. Send email with reset link containing the token
        // 3. Use email service to send the email
        
        log.info("Password reset token generated for user: {}", email);
        log.info("Reset token: {}", resetToken); // In production, this would be sent via email
    }
    
    public void resetPassword(String token, String newPassword) {
        log.info("Processing password reset with token");
        
        try {
            // Validate the reset token
            String email = jwtTokenProvider.validatePasswordResetToken(token);
            
            // Find user by email
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            
            // Validate new password
            if (!isPasswordStrong(newPassword)) {
                throw new BadRequestException("Password does not meet security requirements");
            }
            
            // Update password
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
            
            log.info("Password reset successfully for user: {}", email);
            
        } catch (Exception e) {
            log.error("Invalid or expired reset token", e);
            throw new BadRequestException("Invalid or expired reset token");
        }
    }
    
    public void sendVerificationEmail(User user) {
        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = EmailVerificationToken.builder()
                .token(token)
                .email(user.getEmail())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(24))
                .build();
        emailVerificationTokenRepository.save(verificationToken);
        // Mail gönderimi
            // Test ortamında mail gönderimini atla
            String activeProfile = System.getProperty("spring.profiles.active", System.getenv("SPRING_PROFILES_ACTIVE"));
            if ("test".equals(activeProfile)) {
                log.info("Test ortamında email gönderimi atlandı: {}", user.getEmail());
                return;
            }
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setSubject("Email Verification");
            String verifyUrl = "http://localhost:8080/api/auth/verify-email?token=" + token;
            helper.setText("<p>Merhaba,</p>"
                + "<p>Hesabınızı doğrulamak için aşağıdaki linke tıklayın:</p>"
                + "<p><a href='" + verifyUrl + "'>Emaili Doğrula</a></p>", true);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Verification email could not be sent to {}. Exception: {}", user.getEmail(), e.getMessage(), e);
            // Exception fırlatma, sadece logla
        }
    }

    public boolean verifyEmail(String token) {
        EmailVerificationToken verificationToken = emailVerificationTokenRepository.findByToken(token)
                .orElse(null);
        if (verificationToken == null || verificationToken.getUsed() || verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            return false;
        }
        User user = userRepository.findByEmail(verificationToken.getEmail()).orElse(null);
        if (user == null) {
            return false;
        }
        user.setEmailVerified(true);
        userRepository.save(user);
        verificationToken.setUsed(true);
        emailVerificationTokenRepository.save(verificationToken);
        return true;
    }
}
