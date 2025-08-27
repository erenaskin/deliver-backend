package com.deliver.backend.security;

import com.deliver.backend.entity.User;
import com.deliver.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by username: {}", username);
        
        // Try to find by email first, then by username
        User user = userRepository.findByEmail(username)
                .or(() -> userRepository.findByUsername(username))
                .orElseThrow(() -> {
                    log.error("User not found with username or email: {}", username);
                    return new UsernameNotFoundException("User not found with username or email: " + username);
                });
        
        log.debug("Found user: {} with roles: {}", user.getEmail(), user.getRoles());
        
        return user; // User entity implements UserDetails
    }

    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        log.debug("Loading user by ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new UsernameNotFoundException("User not found with ID: " + id);
                });
        
        log.debug("Found user: {} with roles: {}", user.getEmail(), user.getRoles());
        return user;
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public boolean existsByPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    @Transactional
    public void updateLastLogin(String username) {
        userRepository.findByEmail(username)
                .or(() -> userRepository.findByUsername(username))
                .ifPresent(user -> {
                    user.setLastLoginAt(java.time.LocalDateTime.now());
                    userRepository.save(user);
                    log.debug("Updated last login for user: {}", username);
                });
    }
}
