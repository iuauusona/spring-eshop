package kg.Isagulova.spring_eshop.service;

import jakarta.transaction.Transactional;
import kg.Isagulova.spring_eshop.dao.UserRepository;
import kg.Isagulova.spring_eshop.domain.Role;
import kg.Isagulova.spring_eshop.domain.User;
import kg.Isagulova.spring_eshop.dto.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean save(UserDTO userDTO){
        if(!Objects.equals(userDTO.getPassword(), userDTO.getMatchingPassword())) {
            throw new RuntimeException("Passwords don't match");
        }
        User user = User.builder()
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .email(userDTO.getEmail())
                .role(Role.CLIENT)
                .build();
        userRepository.save(user);
        return true;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public List<UserDTO> getAll(){
        System.out.println("in service impl : ------>>>" + userRepository.findAll());
        return userRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

    }

    private UserDTO toDTO(User user) {
        return UserDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                roles
        );
    }

    @Override
    public User findByName(String name){
        return userRepository.findByUsername(name);//findFirstByName
    }

    @Transactional
    @Override
    public void updateProfile(UserDTO dto){
        User savedUser = userRepository.findByUsername(dto.getUsername());//findFirstByName
        if (savedUser == null) {
            throw new RuntimeException("User not found with username: " + dto.getUsername());
        }
        boolean isChanged = false;
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()){
            savedUser.setPassword(passwordEncoder.encode(dto.getPassword()));
            isChanged = true;
        }
        if(!Objects.equals(dto.getEmail(), savedUser.getEmail())) {
            savedUser.setEmail(dto.getEmail());
            isChanged = true;
        }
        if (isChanged) {
            userRepository.save(savedUser);
        }
    }

}
