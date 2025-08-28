package com.deliver.backend.repository;

import com.deliver.backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryIgnoreCase(String category);

    List<Product> findByIsAvailableTrue();

    Page<Product> findByVendorId(Long vendorId, Pageable pageable);

    List<Product> findByIsFeaturedTrue();

    Page<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description, Pageable pageable);

    List<Product> findByServiceTypeAndIsFeaturedTrue(String serviceType);

    List<Product> findByServiceType(String serviceType);

    // Popülerlik skoruna göre ürünleri getir
    List<Product> findTop10ByServiceTypeOrderByPopularityScoreDesc(String serviceType);

    // Günlük popüler ürünler (son 24 saatte en çok görüntülenen)
    List<Product> findTop10ByServiceTypeAndLastViewedAtAfterOrderByViewCountDesc(String serviceType, LocalDateTime since);

    // Yüksek rating alan ürünler
    List<Product> findTop10ByServiceTypeAndRatingCountGreaterThanOrderByAverageRatingDesc(String serviceType, Integer minRatingCount);

    // Çok satan ürünler
    List<Product> findTop10ByServiceTypeOrderByOrderCountDesc(String serviceType);

    // Trend ürünler (son 7 günde popülerlik skoru artan)
    List<Product> findTop10ByServiceTypeAndCreatedAtAfterOrderByPopularityScoreDesc(String serviceType, LocalDateTime since);
}
