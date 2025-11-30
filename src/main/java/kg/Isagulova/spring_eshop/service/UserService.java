package kg.Isagulova.spring_eshop.service;

import kg.Isagulova.spring_eshop.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService { //security
    boolean save (UserDTO userDTO);
    List<UserDTO> getAll();

}
