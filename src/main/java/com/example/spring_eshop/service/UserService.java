package com.example.spring_eshop.service;

import jakarta.transaction.Transactional;
import com.example.spring_eshop.domain.User;
import com.example.spring_eshop.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService { //security
    boolean save(UserDTO userDTO);

    void save(User user);

    List<UserDTO> getAll();

    User findByName(String name);

    @Transactional
    void updateProfile(UserDTO dto);
}
