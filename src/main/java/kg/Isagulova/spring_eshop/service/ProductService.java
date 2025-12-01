package kg.Isagulova.spring_eshop.service;

import jakarta.transaction.Transactional;
import kg.Isagulova.spring_eshop.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAll();

    @Transactional
    void addToUserBucket(Long productId, String username);
}
