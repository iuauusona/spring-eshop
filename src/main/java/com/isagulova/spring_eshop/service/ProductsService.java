package com.isagulova.spring_eshop.service;

import jakarta.transaction.Transactional;
import com.isagulova.spring_eshop.dto.ProductDTO;

import java.util.List;

public interface ProductsService {
    List<ProductDTO> getAll();

    @Transactional
    void addToUserBucket(Long productId, String username);

    void addProduct(ProductDTO dto);
}
