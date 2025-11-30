package kg.Isagulova.spring_eshop.service;

import kg.Isagulova.spring_eshop.domain.Product;
import kg.Isagulova.spring_eshop.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAll();
}
