package com.deliver.backend.repository;

import com.deliver.backend.entity.Order;
import com.deliver.backend.entity.User;
import com.deliver.backend.entity.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
       long countByStatus(String status);

    Optional<Order> findByOrderNumber(String orderNumber);

    List<Order> findByUser(User user);

    List<Order> findByUserId(Long userId);

    List<Order> findByVendor(Vendor vendor);

    List<Order> findByVendorId(Long vendorId);

    Page<Order> findByUserId(Long userId, Pageable pageable);

    Page<Order> findByVendorId(Long vendorId, Pageable pageable);

    List<Order> findByStatus(Order.OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.status = :status")
    Page<Order> findByStatus(@Param("status") Order.OrderStatus status, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.status = :status")
    List<Order> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Order.OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.vendor.id = :vendorId AND o.status = :status")
    List<Order> findByVendorIdAndStatus(@Param("vendorId") Long vendorId, @Param("status") Order.OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.status = :status")
    Page<Order> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Order.OrderStatus status, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.vendor.id = :vendorId AND o.status = :status")
    Page<Order> findByVendorIdAndStatus(@Param("vendorId") Long vendorId, @Param("status") Order.OrderStatus status, Pageable pageable);

    // Payment related
    List<Order> findByPaymentStatus(Order.PaymentStatus paymentStatus);

    List<Order> findByPaymentMethod(Order.PaymentMethod paymentMethod);

    @Query("SELECT o FROM Order o WHERE o.paymentStatus = 'PENDING' AND o.createdAt < :cutoffTime")
    List<Order> findPendingPaymentOrders(@Param("cutoffTime") LocalDateTime cutoffTime);

    // Date range queries
    @Query("SELECT o FROM Order o WHERE o.createdAt >= :startDate AND o.createdAt <= :endDate")
    List<Order> findOrdersBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.createdAt >= :startDate AND o.createdAt <= :endDate")
    List<Order> findUserOrdersBetween(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT o FROM Order o WHERE o.vendor.id = :vendorId AND o.createdAt >= :startDate AND o.createdAt <= :endDate")
    List<Order> findVendorOrdersBetween(@Param("vendorId") Long vendorId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Delivery queries
    @Query("SELECT o FROM Order o WHERE o.scheduledDeliveryTime >= :startTime AND o.scheduledDeliveryTime <= :endTime")
    List<Order> findOrdersScheduledBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query("SELECT o FROM Order o WHERE o.status IN ('CONFIRMED', 'PREPARING', 'READY', 'PICKED_UP') AND o.scheduledDeliveryTime <= :time")
    List<Order> findActiveOrdersScheduledBefore(@Param("time") LocalDateTime time);

    // Order value queries
    @Query("SELECT o FROM Order o WHERE o.total >= :minAmount")
    List<Order> findOrdersAboveAmount(@Param("minAmount") BigDecimal minAmount);

    @Query("SELECT o FROM Order o WHERE o.total BETWEEN :minAmount AND :maxAmount")
    List<Order> findOrdersInAmountRange(@Param("minAmount") BigDecimal minAmount, @Param("maxAmount") BigDecimal maxAmount);

    // Recent orders
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId ORDER BY o.createdAt DESC")
    Page<Order> findRecentOrdersByUser(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.vendor.id = :vendorId ORDER BY o.createdAt DESC")
    Page<Order> findRecentOrdersByVendor(@Param("vendorId") Long vendorId, Pageable pageable);

    // Analytics and statistics
    @Query("SELECT COUNT(o) FROM Order o WHERE o.vendor.id = :vendorId")
    long countByVendor(@Param("vendorId") Long vendorId);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.user.id = :userId")
    long countByUser(@Param("userId") Long userId);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status")
    long countByStatus(@Param("status") Order.OrderStatus status);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.vendor.id = :vendorId AND o.status = :status")
    long countByVendorAndStatus(@Param("vendorId") Long vendorId, @Param("status") Order.OrderStatus status);

    @Query("SELECT SUM(o.total) FROM Order o WHERE o.vendor.id = :vendorId AND o.status = 'DELIVERED'")
    BigDecimal getTotalRevenueByVendor(@Param("vendorId") Long vendorId);

    @Query("SELECT SUM(o.total) FROM Order o WHERE o.vendor.id = :vendorId AND o.status = 'DELIVERED' AND o.createdAt >= :startDate AND o.createdAt <= :endDate")
    BigDecimal getTotalRevenueByVendorBetween(@Param("vendorId") Long vendorId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT AVG(o.total) FROM Order o WHERE o.vendor.id = :vendorId AND o.status = 'DELIVERED'")
    BigDecimal getAverageOrderValueByVendor(@Param("vendorId") Long vendorId);

    @Query("SELECT AVG(o.rating) FROM Order o WHERE o.vendor.id = :vendorId AND o.rating IS NOT NULL")
    BigDecimal getAverageRatingByVendor(@Param("vendorId") Long vendorId);

    // Customer behavior
    @Query("SELECT COUNT(DISTINCT o.user.id) FROM Order o WHERE o.vendor.id = :vendorId")
    long getUniqueCustomerCountByVendor(@Param("vendorId") Long vendorId);

    @Query("SELECT o.user.id, COUNT(o) FROM Order o WHERE o.vendor.id = :vendorId GROUP BY o.user.id HAVING COUNT(o) > 1")
    List<Object[]> getRepeatCustomersByVendor(@Param("vendorId") Long vendorId);

    // Orders that can be rated
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.status = 'DELIVERED' AND o.rating IS NULL")
    List<Order> findDeliveredOrdersWithoutRating(@Param("userId") Long userId);

    // Promo code usage
    @Query("SELECT o FROM Order o WHERE o.promoCode = :promoCode")
    List<Order> findByPromoCode(@Param("promoCode") String promoCode);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.promoCode = :promoCode")
    long countByPromoCode(@Param("promoCode") String promoCode);

    // Complex analytics queries
    @Query("SELECT DATE(o.createdAt) as orderDate, COUNT(o) as orderCount, SUM(o.total) as totalRevenue " +
           "FROM Order o WHERE o.vendor.id = :vendorId AND o.status = 'DELIVERED' " +
           "AND o.createdAt >= :startDate AND o.createdAt <= :endDate " +
           "GROUP BY DATE(o.createdAt) ORDER BY orderDate")
    List<Object[]> getDailyOrderStatsByVendor(@Param("vendorId") Long vendorId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT HOUR(o.createdAt) as orderHour, COUNT(o) as orderCount " +
           "FROM Order o WHERE o.vendor.id = :vendorId " +
           "AND o.createdAt >= :startDate AND o.createdAt <= :endDate " +
           "GROUP BY HOUR(o.createdAt) ORDER BY orderHour")
    List<Object[]> getHourlyOrderDistributionByVendor(@Param("vendorId") Long vendorId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
