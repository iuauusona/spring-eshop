package com.isagulova.spring_eshop.dao;

import com.isagulova.spring_eshop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);
    User findByActiveCode(String activateCode);
}
