package com.deliver.backend.service;

import com.deliver.backend.dto.request.LoginRequest;
import com.deliver.backend.dto.request.RegisterRequest;
import com.deliver.backend.dto.response.LoginResponse;
import com.deliver.backend.entity.User;
import com.deliver.backend.exception.BadRequestException;
import com.deliver.backend.exception.ResourceNotFoundException;
import com.deliver.backend.repository.UserRepository;
import com.deliver.backend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

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

            log.info("User {} logged in successfully", request.getEmail());

            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(jwtTokenProvider.getTokenExpirationTime())
                    .userId(1L) // Placeholder - would get from authentication
                    .email(request.getEmail())
                    .build();

        } catch (AuthenticationException e) {
            log.error("Authentication failed for user: {}", request.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    @Transactional
    public LoginResponse register(RegisterRequest request) {
        log.info("Attempting registration for user: {}", request.getEmail());

        // Validate input
        validateRegistrationRequest(request);

        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw BadRequestException.emailAlreadyExists();
        }

        // Create new user
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User savedUser = userRepository.save(user);
        log.info("User {} registered successfully", request.getEmail());

        // Generate tokens directly without authentication
        String accessToken = jwtTokenProvider.generateTokenForUser(savedUser);
        String refreshToken = jwtTokenProvider.generateRefreshToken(savedUser.getEmail());

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(3600L)
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
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
                .userId(user.getId())
                .email(user.getEmail())
                .build();
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

    @Transactional
    public void forgotPassword(String email) {
        log.info("Processing forgot password request for email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> ResourceNotFoundException.forUser(email));

        String resetToken = jwtTokenProvider.generatePasswordResetToken(email);

        // Send email with reset token (in a real application, use email service)
        sendPasswordResetEmail(email, resetToken);

        log.info("Password reset token generated and email sent for user: {}", email);
    }

    private void sendPasswordResetEmail(String email, String resetToken) {
        // In a real application, you would use an email service like:
        // - Spring Mail
        // - SendGrid
        // - AWS SES
        // - etc.

        String resetLink = "http://localhost:8080/api/auth/reset-password?token=" + resetToken;
        String emailContent = String.format(
            "Hello,\n\n" +
            "You have requested to reset your password. Click the link below to reset your password:\n\n" +
            "%s\n\n" +
            "This link will expire in 1 hour.\n\n" +
            "If you didn't request this, please ignore this email.\n\n" +
            "Best regards,\n" +
            "Your Application Team",
            resetLink
        );

        // Simulate email sending
        log.info("Sending password reset email to: {}", email);
        log.debug("Email content: {}", emailContent);

        // In production, replace with actual email service:
        // emailService.send(email, "Password Reset", emailContent);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        log.info("Processing password reset");

        try {
            String email = jwtTokenProvider.validatePasswordResetToken(token);

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> ResourceNotFoundException.forUser(email));

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            log.info("Password reset successfully for user: {}", email);
        } catch (Exception e) {
            log.error("Invalid password reset token: {}", e.getMessage());
            throw new BadRequestException("Invalid or expired reset token");
        }
    }

    @Transactional
    public boolean verifyEmail(String token) {
        log.info("Processing email verification");

        try {
            String email = jwtTokenProvider.validateEmailVerificationToken(token);

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> ResourceNotFoundException.forUser(email));

            user.setEnabled(true);
            userRepository.save(user);

            log.info("Email verified successfully for user: {}", email);
            return true;
        } catch (Exception e) {
            log.error("Invalid email verification token: {}", e.getMessage());
            return false;
        }
    }
}
