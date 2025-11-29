package kg.Isagulova.spring_eshop.dao;

import kg.Isagulova.spring_eshop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

}
