package com.isagulova.spring_eshop.service;

import jakarta.transaction.Transactional;
import com.isagulova.spring_eshop.dao.UserRepository;
import com.isagulova.spring_eshop.domain.Role;
import com.isagulova.spring_eshop.domain.User;
import com.isagulova.spring_eshop.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSenderService mailSenderService;


    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, MailSenderService mailSenderService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSenderService = mailSenderService;
    }

    @Override
    public boolean save(UserDTO userDTO) {
        if (!Objects.equals(userDTO.getPassword(), userDTO.getMatchingPassword())) {
            throw new RuntimeException("Passwords don't match");
        }
        User user = User.builder()
                .name(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .email(userDTO.getEmail())
                .role(userDTO.getRole())
                .activeCode(UUID.randomUUID().toString())
                .address(userDTO.getAddress())
                .phoneNumber(userDTO.getPhoneNumber())
                .build();
        userRepository.save(user);
        mailSenderService.sendActivateCode(user);
        return true;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public List<UserDTO> getAll() {
        return userRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

    }

    @Override
    public UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .role(user.getRole())
                .archived(user.isArchived())
                .activated(user.getActiveCode() == null)
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPassword(),
                roles
        );
    }

    @Override
    public User findByName(String name) {
        return userRepository.findByName(name);//findFirstByName
    }

    @Override
    public boolean activateUser(String activateCode) {
        User user = userRepository.findByActiveCode(activateCode);
        if (user == null) {
            return false;
        }

        user.setActiveCode(null); // или active=true
        userRepository.save(user);
        return true;
    }

    @Override
    public void deleteById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void updateById(Long id, UserDTO dto) {
        User savedUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        boolean isChanged = false;
        boolean usernameChanged = false;
        boolean emailChanged = false;

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            savedUser.setPassword(passwordEncoder.encode(dto.getPassword()));
            isChanged = true;
        }

        if (!Objects.equals(dto.getAddress(), savedUser.getAddress())) {
            savedUser.setAddress(dto.getAddress());
            isChanged = true;
        }

        if (!Objects.equals(dto.getPhoneNumber(), savedUser.getPhoneNumber())) {
            savedUser.setPhoneNumber(dto.getPhoneNumber());
            isChanged = true;
        }
        if (!Objects.equals(dto.getRole(), savedUser.getRole())) {
            savedUser.setRole(dto.getRole());
            isChanged = true;
        }

        if (!Objects.equals(dto.getEmail(), savedUser.getEmail())) {
            savedUser.setEmail(dto.getEmail());
            savedUser.setActiveCode(UUID.randomUUID().toString());
            emailChanged = true;
            isChanged = true;
        }

        if (!Objects.equals(dto.getUsername(), savedUser.getName())) {
            savedUser.setName(dto.getUsername());
            usernameChanged = true;
            isChanged = true;
        }

        if (isChanged) {
            userRepository.save(savedUser);
        }

        if (emailChanged) {
            mailSenderService.sendActivateCode(savedUser);
        }

        // ⬇️ ВАЖНО: обновляем SecurityContext
        if (usernameChanged) {
            updateAuthentication(savedUser);
        }
    }


    private void updateAuthentication(User user) {
        Authentication currentAuth =
                SecurityContextHolder.getContext().getAuthentication();

        UsernamePasswordAuthenticationToken newAuth =
                new UsernamePasswordAuthenticationToken(
                        user.getName(), // 🔥 новый username
                        currentAuth.getCredentials(),
                        currentAuth.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

}
