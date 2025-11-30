package kg.Isagulova.spring_eshop.dao;

import kg.Isagulova.spring_eshop.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
