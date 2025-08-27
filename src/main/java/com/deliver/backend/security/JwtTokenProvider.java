package com.deliver.backend.security;

import com.deliver.backend.config.JwtConfig;
import com.deliver.backend.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    private final JwtConfig jwtConfig;
    private final UserRepository userRepository;


    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
    }

    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return generateTokenForUser(userPrincipal);
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    public String generateTokenForUser(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        
        // Add roles to claims
        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        claims.put("roles", authorities);
        claims.put("enabled", userDetails.isEnabled());
        
        return createToken(claims, userDetails.getUsername());
    }

    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        
        Instant now = Instant.now();
        Instant expiration = now.plus(jwtConfig.getRefreshExpiration(), ChronoUnit.MILLIS);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Instant now = Instant.now();
        Instant expiration = now.plus(jwtConfig.getExpiration(), ChronoUnit.MILLIS);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .setIssuer(jwtConfig.getIssuer())
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);

            // Ek kontrol: Kullanıcı gerçekten var mı?
            String email = getUsernameFromToken(token);
            boolean userExists = userRepository.existsByEmail(email);

            if (!userExists) {
                log.warn("Token valid but user not found: {}", email);
                return false;
            }

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            log.error("Error extracting username from token: {}", e.getMessage());
            return null;
        }
    }

    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Error extracting claims from token: {}", e.getMessage());
            return null;
        }
    }

    public String getRolesFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? (String) claims.get("roles") : null;
    }

    public Date getExpirationDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? claims.getExpiration() : null;
    }

    public boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration != null && expiration.before(new Date());
    }

    public boolean canTokenBeRefreshed(String token) {
        return !isTokenExpired(token);
    }

    public long getTokenExpirationTime() {
        return jwtConfig.getExpiration();
    }

    public long getRefreshTokenExpirationTime() {
        return jwtConfig.getRefreshExpiration();
    }
    
    /**
     * Generate password reset token (valid for 1 hour)
     */
    public String generatePasswordResetToken(String email) {
        long resetTokenExpiration = 3600000; // 1 hour in milliseconds
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + resetTokenExpiration))
                .claim("type", "password_reset")
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    
    /**
     * Generate email verification token (valid for 24 hours)
     */
    public String generateEmailVerificationToken(String email) {
        long verificationTokenExpiration = 86400000; // 24 hours in milliseconds
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + verificationTokenExpiration))
                .claim("type", "email_verification")
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    
    /**
     * Validate password reset token and return email
     */
    public String validatePasswordResetToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims == null || !"password_reset".equals(claims.get("type"))) {
            throw new IllegalArgumentException("Invalid password reset token");
        }
        return claims.getSubject();
    }
    
    /**
     * Validate email verification token and return email
     */
    public String validateEmailVerificationToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims == null || !"email_verification".equals(claims.get("type"))) {
            throw new IllegalArgumentException("Invalid email verification token");
        }
        return claims.getSubject();
    }
}
