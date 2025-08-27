package com.deliver.backend.repository;

import com.deliver.backend.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    List<ProductCategory> findByServiceCategory_IdAndIsActiveTrueOrderByDisplayOrderAsc(Long serviceCategoryId);

    List<ProductCategory> findByServiceCategory_IdAndIsActiveTrue(Long serviceCategoryId);

    @Query("SELECT pc FROM ProductCategory pc WHERE pc.serviceCategory.id = :serviceCategoryId AND pc.isActive = true ORDER BY pc.displayOrder ASC")
    List<ProductCategory> findActiveCategoriesByServiceOrdered(@Param("serviceCategoryId") Long serviceCategoryId);

    List<ProductCategory> findByNameContainingIgnoreCaseAndIsActiveTrue(String name);
}

