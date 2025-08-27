package com.deliver.backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    private String secret = "2b7e151628aed2a6abf7158809cf4f3c2b7e151628aed2a6abf7158809cf4f3c2b7e151628aed2a6abf7158809cf4f3c";
    private long expiration = 86400000;
    private long refreshExpiration = 604800000;
    private String issuer = "deliver-backend";
    private String audience = "deliver-app";
    
    public long getExpirationInSeconds() {
        return expiration / 1000;
    }
    
    public long getRefreshExpirationInSeconds() {
        return refreshExpiration / 1000;
    }
}
