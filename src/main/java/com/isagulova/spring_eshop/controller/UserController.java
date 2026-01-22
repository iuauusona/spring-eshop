package com.isagulova.spring_eshop.controller;


import com.isagulova.spring_eshop.dao.UserRepository;
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
import java.util.Optional;

@Controller
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
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
                .id(user.getId())
                .username(user.getName())
                .email(user.getEmail())
                .address(user.getAddress())
                .role(user.getRole())
                .phoneNumber(user.getPhoneNumber())
                .activated(user.getActiveCode() == null)
                .build();
        model.addAttribute("users", userDTO);
        return "profile";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/profile")
    public String updateProfileUser(UserDTO dto, Model model, Principal principal) {
        if (principal == null) {
            throw new RuntimeException("You are not authorized");
        }
        if (dto.getPassword() != null
                && !dto.getPassword().isEmpty()
                && !Objects.equals(dto.getPassword(), dto.getMatchingPassword())) {
            model.addAttribute("users", dto);
            //нужно добавить какое-то сообщение, но сделаем это другой раз
            return "profile";
        }
        userService.updateById(dto.getId(), dto, true);
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
    @PostMapping("/save")
    public String saveOrUpdateUser(@ModelAttribute("user") UserDTO dto, Model model) {
        if (dto.getPassword() != null
                && !dto.getPassword().isEmpty()
                && !Objects.equals(dto.getPassword(), dto.getMatchingPassword())) {

            model.addAttribute("roles", List.of("ADMIN", "MANAGER", "CLIENT"));
            model.addAttribute("editMode", dto.getId() != null);
            return "admin-users-page";
        }

        if (dto.getId() == null) {
            userService.save(dto);
            System.out.println("User saved: " + dto);
        } else {
            userService.updateById(dto.getId(), dto, false);
        }
        return "redirect:/users";
    }

    // Удаление по ID
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/users";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/update/{id}")
    public String editUser(@PathVariable Long id, Model model) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        if (user == null) {
            new RuntimeException("User with id " + id + " not found");
        }
        UserDTO dto = userService.toDTO(user);

        model.addAttribute("user", dto);
        model.addAttribute("roles", List.of("ADMIN", "MANAGER", "CLIENT"));
        model.addAttribute("editMode", true); // 🔥 важно

        return "admin-users-page";
    }



    @GetMapping("/activate/{code}")
    public String activateUser(Model model, @PathVariable("code") String activateCode){
        boolean activated = userService.activateUser(activateCode);
        model.addAttribute("activated", activated);
        return "feedbackAboutActivation";
    }
}
