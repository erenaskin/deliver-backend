package com.deliver.backend.repository;

import com.deliver.backend.entity.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    
    // Find by service ID
    List<Vendor> findByServiceId(Long serviceId);
    
    // Find by service ID and accepting orders
    List<Vendor> findByServiceIdAndIsAcceptingOrdersTrue(Long serviceId);
    
    // Find by category
    List<Vendor> findByCategoryIgnoreCase(String category);
    
    // Find by service type
    List<Vendor> findByServiceTypeIgnoreCase(String serviceType);
    
    // Find by delivery fee range
    List<Vendor> findByDeliveryFeeBetween(BigDecimal minFee, BigDecimal maxFee);
    
    // Find by rating
    List<Vendor> findByAverageRatingGreaterThanEqual(BigDecimal minRating);
    
    // Find 24/7 vendors
    List<Vendor> findByIs24HoursTrue();
    
    // Find vendors with prescription
    List<Vendor> findByIsPrescriptionAvailableTrue();
    
    // Find vendors with vet service
    List<Vendor> findByIsVetServiceAvailableTrue();
    
    // Find vendors with installment
    List<Vendor> findByIsInstallmentAvailableTrue();
    
    // Find vendors with discount
    List<Vendor> findByHasDiscountTrue();
    
    // Search vendors
    @Query("SELECT v FROM Vendor v WHERE v.isAcceptingOrders = true AND " +
           "(:query IS NULL OR LOWER(v.businessName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(v.description) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:serviceCategory IS NULL OR v.serviceType = :serviceCategory)")
    Page<Vendor> searchVendors(@Param("query") String query, 
                               @Param("serviceCategory") String serviceCategory,
                               Pageable pageable);
}
