package com.isagulova.spring_eshop.dao;

import com.isagulova.spring_eshop.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}