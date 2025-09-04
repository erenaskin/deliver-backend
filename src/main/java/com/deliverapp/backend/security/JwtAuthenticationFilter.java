package com.deliverapp.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final TokenBlacklist tokenBlacklist;

    // Public endpoint'ler - bunlar için JWT kontrolü yapılmayacak
    private final List<String> publicEndpoints = Arrays.asList(
            "/api/auth/register",
            "/api/auth/login",
            "/api/auth/verify-email/send",
            "/api/auth/verify-email/confirm",
            "/api/auth/reset-password/send",
            "/api/auth/reset-password/confirm",
            "/actuator/health",
            "/health"
    );

    // Configurable header and prefix
    private final String headerName = "Authorization";
    private final String tokenPrefix = "Bearer ";

    @Override
    protected void doFilterInternal(@org.springframework.lang.NonNull HttpServletRequest request,
                                    @org.springframework.lang.NonNull HttpServletResponse response,
                                    @org.springframework.lang.NonNull FilterChain filterChain) throws ServletException, IOException {

        String requestPath = request.getRequestURI();
        logger.debug("JwtAuthenticationFilter - Request path: {}", requestPath);

        // Public endpoint'ler için JWT kontrolü yapma
        if (isPublicEndpoint(requestPath)) {
            logger.debug("Public endpoint detected, JWT filter bypass: {}", requestPath);
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(headerName);

        if (authHeader == null || !authHeader.startsWith(tokenPrefix)) {
            logger.debug("{} header eksik veya {} ile başlamıyor", headerName, tokenPrefix);
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(tokenPrefix.length());

        // Blacklist kontrolü
        if (tokenBlacklist.isBlacklisted(token)) {
            logger.warn("Token blacklist'te: {}", token);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token iptal edilmiş veya geçersiz");
            return;
        }

        String username = null;
        try {
            username = jwtTokenProvider.getUsernameFromToken(token);
        } catch (Exception e) {
            logger.warn("JWT token çözümlenirken hata: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Geçersiz veya bozuk token");
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = null;
            try {
                userDetails = userDetailsService.loadUserByUsername(username);
            } catch (Exception e) {
                logger.warn("Kullanıcı bulunamadı: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Kullanıcı bulunamadı");
                return;
            }

            if (jwtTokenProvider.validateToken(token)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.debug("JWT doğrulandı ve authentication set edildi: {}", username);
            } else {
                logger.warn("JWT doğrulama başarısız: {}", token);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token doğrulama başarısız");
                return;
            }
        }

        filterChain.doFilter(request, response);
        logger.debug("JwtAuthenticationFilter tamamlandı");
    }

    /**
     * Public endpoint kontrolü - bu endpoint'ler için JWT filter bypass edilir
     */
    private boolean isPublicEndpoint(String requestPath) {
        return publicEndpoints.stream().anyMatch(endpoint -> {
            // Query parameter'li endpoint'ler için (örn: /verify-email/confirm?code=123)
            if (requestPath.contains("?")) {
                String pathWithoutQuery = requestPath.substring(0, requestPath.indexOf("?"));
                return pathWithoutQuery.equals(endpoint);
            }
            return requestPath.equals(endpoint);
        });
    }
}