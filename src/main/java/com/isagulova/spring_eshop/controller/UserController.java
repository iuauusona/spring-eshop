package com.isagulova.spring_eshop.controller;


import com.isagulova.spring_eshop.domain.User;
import com.isagulova.spring_eshop.dto.UserDTO;
import com.isagulova.spring_eshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Objects;

@Controller
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String newModel(Model model) {
        System.out.println("Called method newUser");
        model.addAttribute("user", new UserDTO());
        return "user";
    }

    @PostAuthorize("isAuthenticated() and #username == authentication.principal.username")
    @GetMapping("/{name}/roles")
    @ResponseBody
    public String getRoles(@PathVariable("name") String username) {
        System.out.println("Called method getRoles");
        User byName = userService.findByName(username);
        System.out.println(byName.getRole().name());
        return byName.getRole().name();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String profileUser(Model model, Principal principal) {
        if (principal == null) {
            throw new RuntimeException("You are not authorized");
        }
        User user = userService.findByName(principal.getName());

        UserDTO userDTO = UserDTO.builder()
                .username(user.getName())
                .email(user.getEmail())
                .activated(user.getActiveCode() == null)
                .build();
        model.addAttribute("user", userDTO);
        return "profile";
    }

    @PostAuthorize("isAuthenticated()")
    @PostMapping("/profile")
    public String updateProfileUser(UserDTO dto, Model model, Principal principal) {
        if (principal == null || !Objects.equals(principal.getName(), dto.getUsername())) {
            throw new RuntimeException("You are not authorized");
        }
        if (dto.getPassword() != null
                && !dto.getPassword().isEmpty()
                && !Objects.equals(dto.getPassword(), dto.getMatchingPassword())) {
            model.addAttribute("user", dto);
            //нужно добавить какое-то сообщение, но сделаем это другой раз
            return "profile";
        }
        userService.updateProfile(dto);
        return "redirect:/users/profile";
    }

    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userService.getAll());
        return "userList";
    }

    @PostMapping("/new")
    public String saveUser(UserDTO dto, Model model) {
        if (userService.save(dto)) {
            return "redirect:/users";
        } else {
            model.addAttribute("user", dto);
            return "user";
        }
    }

    @GetMapping("/activate/{code}")
    public String activateUser(Model model, @PathVariable("code") String activateCode){
        boolean activated = userService.activateUser(activateCode);
        model.addAttribute("activated", activated);
        return "active-user";
    }
}
