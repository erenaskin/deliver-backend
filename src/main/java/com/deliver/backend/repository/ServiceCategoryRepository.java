package com.deliver.backend.repository;

import com.deliver.backend.entity.ServiceCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {
    
    // Find active service categories ordered by display order
    List<ServiceCategory> findByIsActiveTrueOrderByDisplayOrder();
    
    // Find by name
    Optional<ServiceCategory> findByNameIgnoreCase(String name);
    
    // Find emergency services
    List<ServiceCategory> findByIsEmergencyServiceTrueAndIsActiveTrueOrderByDisplayOrder();
    
    // Find with pagination
    Page<ServiceCategory> findByIsActiveTrueOrderByDisplayOrder(Pageable pageable);
    
    // Check if exists by name
    boolean existsByNameIgnoreCase(String name);
    
    // Count active service categories
    long countByIsActiveTrue();
    
    // Find top N service categories by order
    @Query("SELECT sc FROM ServiceCategory sc WHERE sc.isActive = true ORDER BY sc.displayOrder LIMIT :limit")
    List<ServiceCategory> findTopNByIsActiveTrueOrderByDisplayOrder(@Param("limit") int limit);
    
    // Find service categories with specific features
    @Query("SELECT sc FROM ServiceCategory sc WHERE sc.isActive = true AND sc.isEmergencyService = :emergency ORDER BY sc.displayOrder")
    List<ServiceCategory> findByIsActiveTrueAndIsEmergencyService(@Param("emergency") boolean emergency);
}

