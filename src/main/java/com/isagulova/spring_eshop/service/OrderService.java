package com.isagulova.spring_eshop.service;

import com.isagulova.spring_eshop.domain.Order;

public interface OrderService {
    void saveOrder(Order order);
    Order getOrder(Long id);
}