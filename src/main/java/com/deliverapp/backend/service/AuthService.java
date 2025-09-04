package com.deliverapp.backend.service;


import com.deliverapp.backend.dto.request.LoginRequest;
import com.deliverapp.backend.dto.request.RegisterRequest;
import com.deliverapp.backend.model.Role;
import com.deliverapp.backend.model.User;
import com.deliverapp.backend.repository.UserRepository;
import com.deliverapp.backend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Transactional
    public void deleteUser(Long userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı"));
            userRepository.delete(user);
            auditLogService.log("DELETE", user.getUsername(), "Kullanıcı hesabı silindi");
        } catch (Exception e) {
            auditLogService.log("DELETE-FAIL", String.valueOf(userId), "Kullanıcı silme başarısız: " + e.getMessage());
            throw e;
        }
    }

    public String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final AuditLogService auditLogService;

    @Transactional
    public String register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            auditLogService.log("REGISTER-FAIL", request.getUsername(), "Email zaten kayıtlı");
            throw new IllegalArgumentException("Email zaten kayıtlı");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            auditLogService.log("REGISTER-FAIL", request.getUsername(), "Username zaten kayıtlı");
            throw new IllegalArgumentException("Username zaten kayıtlı");
        }

    User user = User.builder()
        .username(request.getUsername())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .build();

        userRepository.save(user);
        auditLogService.log("REGISTER", user.getUsername(), "Kullanıcı kaydı oluşturuldu");

        return jwtTokenProvider.generateToken(user);
    }
    public String login(LoginRequest request) {
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            auditLogService.log("LOGIN-FAIL", "", "Email alanı boş!");
            throw new IllegalArgumentException("Email alanı boş!");
        }
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            User user = (User) auth.getPrincipal();
            auditLogService.log("LOGIN", user.getUsername(), "Kullanıcı giriş yaptı");
            return jwtTokenProvider.generateToken(user);
        } catch (Exception e) {
            auditLogService.log("LOGIN-FAIL", request.getEmail(), "Giriş başarısız: " + e.getMessage());
            throw e;
        }
    }
}

