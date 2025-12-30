package com.isagulova.spring_eshop.dao;

import com.isagulova.spring_eshop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);
    User findByActiveCode(String activateCode);
    Optional<User> findById(Long id);
}
