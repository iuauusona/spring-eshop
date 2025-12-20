package com.isagulova.springintegration.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private long orderId;
    private String username;
    private String address;
    private List<OrderDetails> details;
}