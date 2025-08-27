package com.deliver.backend.repository;

import com.deliver.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByPhoneNumber(String phoneNumber);
    
    boolean existsByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByPhoneNumber(String phoneNumber);
    
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = :role")
    List<User> findByRole(@Param("role") User.UserRole role);
    
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = :role")
    Page<User> findByRole(@Param("role") User.UserRole role, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE'")
    List<User> findActiveUsers();
    
    @Query("SELECT u FROM User u WHERE u.status = 'INACTIVE'")
    List<User> findInactiveUsers();
    
    @Query("SELECT u FROM User u WHERE u.createdAt >= :startDate AND u.createdAt <= :endDate")
    List<User> findUsersCreatedBetween(@Param("startDate") LocalDateTime startDate, 
                                       @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT u FROM User u WHERE u.lastLoginAt >= :since")
    List<User> findRecentlyActiveUsers(@Param("since") LocalDateTime since);
    
    @Query("SELECT u FROM User u WHERE u.firstName LIKE %:keyword% OR u.lastName LIKE %:keyword% OR u.email LIKE %:keyword%")
    Page<User> searchUsers(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r = :role")
    long countByRole(@Param("role") User.UserRole role);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.status = 'ACTIVE'")
    long countActiveUsers();
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :startDate")
    long countUsersCreatedAfter(@Param("startDate") LocalDateTime startDate);
    
    // Find users by address components (address is a simple string field)
    @Query("SELECT u FROM User u WHERE u.address LIKE %:city%")
    List<User> findByCity(@Param("city") String city);
    
    @Query("SELECT u FROM User u WHERE u.address LIKE %:state%")
    List<User> findByState(@Param("state") String state);
    
    @Query("SELECT u FROM User u WHERE u.address LIKE %:zipCode%")
    List<User> findByZipCode(@Param("zipCode") String zipCode);
    
    // Find customers who have made orders (assuming orders relationship exists)
    @Query("SELECT DISTINCT u FROM User u JOIN u.roles r WHERE r = 'USER' AND EXISTS (SELECT o FROM Order o WHERE o.user = u)")
    List<User> findCustomersWithOrders();
    
    // Future: Find users within delivery radius (requires lat/lng fields)
    // Currently disabled as User entity doesn't have coordinates
    /*
    @Query(value = """
        SELECT * FROM users u 
        WHERE u.address_latitude IS NOT NULL 
        AND u.address_longitude IS NOT NULL
        AND (6371 * acos(cos(radians(:lat)) * cos(radians(u.address_latitude)) 
        * cos(radians(u.address_longitude) - radians(:lng)) 
        + sin(radians(:lat)) * sin(radians(u.address_latitude)))) <= :radiusKm
        """, nativeQuery = true)
    List<User> findUsersWithinRadius(@Param("lat") Double latitude, 
                                     @Param("lng") Double longitude, 
                                     @Param("radiusKm") Double radiusKm);
    */
}
