package com.deliver.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User registration request payload")
public class RegisterRequest {

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Username can only contain letters, numbers, dots, underscores and hyphens")
    @Schema(description = "Unique username", example = "johndoe")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Schema(description = "User email address", example = "john.doe@example.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "Password must contain at least one lowercase letter, one uppercase letter, and one digit")
    @Schema(description = "User password (must contain uppercase, lowercase, and digit)", example = "Password123")
    private String password;

    @NotBlank(message = "Password confirmation is required")
    @Schema(description = "Password confirmation", example = "Password123")
    private String confirmPassword;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number must be valid")
    @Schema(description = "User phone number", example = "+1234567890")
    private String phoneNumber;

    @Size(max = 255, message = "Address must not exceed 255 characters")
    @Schema(description = "User address", example = "123 Main St, City, State, ZIP")
    private String address;

    @Past(message = "Birth date must be in the past")
    @Schema(description = "User birth date", example = "1990-01-01")
    private java.time.LocalDate birthDate;

    @AssertTrue(message = "You must accept the terms and conditions")
    @Schema(description = "Terms and conditions acceptance", example = "true")
    private Boolean acceptTerms;

    @Schema(description = "Newsletter subscription preference", example = "true")
    @Builder.Default
    private Boolean subscribeNewsletter = false;

    // Custom validation method
    @AssertTrue(message = "Password and confirmation password must match")
    private boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }
}
