package com.deliver.backend.repository;

import com.deliver.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    long countByStatus(Order.OrderStatus status);
}
