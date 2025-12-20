package com.isagulova.springintegration.service;

import com.isagulova.springintegration.domain.Order;

import java.io.IOException;

public interface OrderService {
    void save(Order order) throws IOException;
}