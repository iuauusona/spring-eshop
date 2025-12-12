package com.example.spring_eshop.service;

import com.example.spring_eshop.dao.OrderRepository;
import com.example.spring_eshop.domain.Order;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }
}
