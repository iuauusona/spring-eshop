package com.isagulova.spring_eshop.dao;

import com.isagulova.spring_eshop.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
