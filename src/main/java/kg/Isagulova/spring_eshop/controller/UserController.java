package kg.Isagulova.spring_eshop.controller;


import kg.Isagulova.spring_eshop.domain.User;
import kg.Isagulova.spring_eshop.dto.UserDTO;
import kg.Isagulova.spring_eshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Objects;

@Controller
@RequestMapping("users")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/new")
    public String newModel(Model model){
        model.addAttribute("user", new UserDTO());
        return "user";
    }

    @GetMapping("/profile")
    public String profileUser(Model model, Principal principal){
        if (principal == null){
            throw new RuntimeException("You are not authorized");
        }
        User user = userService.findByName(principal.getName());

        UserDTO userDTO = UserDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
        model.addAttribute("user", userDTO);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfileUser(UserDTO dto, Model model, Principal principal){
        if (principal == null || !Objects.equals(principal.getName(), dto.getUsername())){
            throw new RuntimeException("You are not authorized");
        }
        if(dto.getPassword() != null
                && !dto.getPassword().isEmpty()
                && !Objects.equals(dto.getPassword(), dto.getMatchingPassword())){
            model.addAttribute("user", dto);
            //нужно добавить какое-то сообщение, но сделаем это другой раз
            return "profile";
        }
        userService.updateProfile(dto);
        return "redirect:/users/profile";
    }

    @GetMapping
    public String userList(Model model){
        model.addAttribute("users", userService.getAll());
        return "userList";
    }

    @PostMapping("/new")
    public String saveUser(UserDTO dto, Model model){
        if(userService.save(dto)) {
            return "redirect:/users";
        }else{
            model.addAttribute("user", dto);
            return "user";
        }
    }
}
