package com.isagulova.spring_eshop.controller;


import com.isagulova.spring_eshop.domain.User;
import com.isagulova.spring_eshop.dto.UserDTO;
import com.isagulova.spring_eshop.service.UserService;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @PreAuthorize("hasRole('ADMIN')")
//    @GetMapping("/new")
//    public String newModel(Model model) {
//        System.out.println("Called method newUser");
//        model.addAttribute("user", new UserDTO());
//        return "user";
//    }

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
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .activated(user.getActiveCode() == null)
                .build();
        model.addAttribute("users", userDTO);
        return "profile";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/profile")
    public String updateProfileUser(UserDTO dto, Model model, Principal principal) {
        if (principal == null || !Objects.equals(principal.getName(), dto.getUsername())) {
            throw new RuntimeException("You are not authorized");
        }
        if (dto.getPassword() != null
                && !dto.getPassword().isEmpty()
                && !Objects.equals(dto.getPassword(), dto.getMatchingPassword())) {
            model.addAttribute("users", dto);
            //нужно добавить какое-то сообщение, но сделаем это другой раз
            return "profile";
        }
        userService.updateProfile(dto);
        return "redirect:/users/profile";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/adminDash")
    public String getAdminDash(){
        return "admin-dashboard";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userService.getAll());
        model.addAttribute("user", new UserDTO());
        model.addAttribute("roles", List.of("ADMIN", "MANAGER", "CLIENT"));
        System.out.println("Called method getAll(): " + userService.getAll());
        return "admin-users-page";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/new")
    public String saveUser(UserDTO dto, Model model) {
        if (userService.save(dto)) {
            return "redirect:/users";
        } else {
            model.addAttribute("user", dto);
            return "admin-users-page";
        }
    }
    // Удаление по ID
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{username}")
    public String deleteUser(@PathVariable String username) {
        userService.deleteById(username);
        return "redirect:/users";
    }

//    @PostMapping("/update/{username}")
//    public String updateUser(@PathVariable String username,  UserDTO dto, Model model) {
//        if (dto.getPassword() != null
//                && !dto.getPassword().isEmpty()
//                && !Objects.equals(dto.getPassword(), dto.getMatchingPassword())) {
//            model.addAttribute("users", dto);
//            return "admin-users-page";
//        }
//        userService.updateById(username);
//        return "redirect:/users";
//    }
//    @GetMapping("/update/{username}")
//    public String updateUser(@PathVariable String username, Model model) {
//        User user = userService.findByName(username);
//        UserDTO dto = userService.toDTO(user);
//        model.addAttribute("user", dto);
//        model.addAttribute("roles", List.of("ADMIN", "MANAGER", "CLIENT"));
//        return "admin-users-page"; // та же страница с формой
//    }


    @GetMapping("/activate/{code}")
    public String activateUser(Model model, @PathVariable("code") String activateCode){
        boolean activated = userService.activateUser(activateCode);
        model.addAttribute("activated", activated);
        return "feedbackAboutActivation";
    }
}
