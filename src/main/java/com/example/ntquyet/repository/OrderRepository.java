package com.example.ntquyet.repository;

import com.example.ntquyet.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository cho Order.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

}