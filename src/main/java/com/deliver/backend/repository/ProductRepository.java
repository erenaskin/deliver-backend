package com.deliver.backend.repository;

import com.deliver.backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Find by service ID
    Page<Product> findByServiceId(Long serviceId, Pageable pageable);
    
    // Find by service ID and category
    Page<Product> findByServiceIdAndCategoryIgnoreCase(Long serviceId, String category, Pageable pageable);
    
    // Find by service ID and pet type
    Page<Product> findByServiceIdAndPetTypeIgnoreCase(Long serviceId, String petType, Pageable pageable);
    
    // Find by service ID and water type
    Page<Product> findByServiceIdAndWaterTypeIgnoreCase(Long serviceId, String waterType, Pageable pageable);
    
    // Find by vendor ID
    List<Product> findByVendorId(Long vendorId);
    
    // Find by category
    List<Product> findByCategoryIgnoreCase(String category);
    
    // Find by price range
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    // Find featured products
    List<Product> findByIsFeaturedTrue();
    
    // Find available products
    List<Product> findByIsAvailableTrue();
    
    // Search products
    @Query("SELECT p FROM Product p WHERE p.isAvailable = true AND " +
           "(:query IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:serviceCategory IS NULL OR p.service.category = :serviceCategory) AND " +
           "(:productCategory IS NULL OR p.category = :productCategory) AND " +
           "(:petType IS NULL OR p.petType = :petType)")
    Page<Product> searchProducts(@Param("query") String query, 
                                @Param("serviceCategory") String serviceCategory,
                                @Param("productCategory") String productCategory,
                                @Param("petType") String petType,
                                Pageable pageable);
}
