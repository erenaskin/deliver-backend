package com.deliver.backend.repository;

import com.deliver.backend.entity.PetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetTypeRepository extends JpaRepository<PetType, Long> {
    
    List<PetType> findByServiceIdAndIsActiveTrueOrderByDisplayOrderAsc(Long serviceId);
    
    List<PetType> findByServiceIdAndIsActiveTrue(Long serviceId);
    
    @Query("SELECT pt FROM PetType pt WHERE pt.service.id = :serviceId AND pt.isActive = true ORDER BY pt.displayOrder ASC")
    List<PetType> findActivePetTypesByServiceOrdered(@Param("serviceId") Long serviceId);
    
    PetType findByNameAndServiceIdAndIsActiveTrue(String name, Long serviceId);
}
