package com.deliver.backend.repository;

import com.deliver.backend.entity.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    
    // Find active services ordered by order
    List<Service> findByIsActiveTrueOrderByOrder();
    
    // Find by category
    Optional<Service> findByCategoryIgnoreCaseAndIsActiveTrue(String category);
    
    // Find by category
    List<Service> findByCategoryAndIsActiveTrue(String category);
    
    // Find with pagination
    Page<Service> findByIsActiveTrueOrderByOrder(Pageable pageable);
    
    // Check if exists by category
    boolean existsByCategoryIgnoreCase(String category);
    
    // Count active services
    long countByIsActiveTrue();
    
    // Find top N services by rating
    @Query("SELECT s FROM Service s WHERE s.isActive = true ORDER BY s.averageRating DESC LIMIT :limit")
    List<Service> findTopByOrderByAverageRatingDesc(@Param("limit") int limit);
}
