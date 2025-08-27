package com.deliver.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Login response payload")
public class LoginResponse {

    @Schema(description = "JWT access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    @Schema(description = "JWT refresh token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;

    @Schema(description = "Token type", example = "Bearer")
    @Builder.Default
    private String tokenType = "Bearer";

    @Schema(description = "Token expiration time in seconds", example = "3600")
    private Long expiresIn;

    @Schema(description = "User information")
    private UserInfo user;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "User information")
    public static class UserInfo {

        @Schema(description = "User ID", example = "1")
        private Long id;

        @Schema(description = "Username", example = "johndoe")
        private String username;

        @Schema(description = "Email address", example = "john.doe@example.com")
        private String email;

        @Schema(description = "First name", example = "John")
        private String firstName;

        @Schema(description = "Last name", example = "Doe")
        private String lastName;

        @Schema(description = "User roles", example = "[\"USER\", \"VENDOR\"]")
        private Set<String> roles;

        @Schema(description = "Email verification status", example = "true")
        private Boolean emailVerified;

        @Schema(description = "Account status", example = "ACTIVE")
        private String status;

        @Schema(description = "Profile picture URL", example = "https://example.com/avatar.jpg")
        private String profilePictureUrl;

        @Schema(description = "Last login time")
        private LocalDateTime lastLoginAt;

        @Schema(description = "Account creation time")
        private LocalDateTime createdAt;
    }
}
