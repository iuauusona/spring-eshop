package com.isagulova.spring_eshop.controller;

import com.isagulova.spring_eshop.dto.UserDTO;
import com.isagulova.spring_eshop.service.UserService;
import jakarta.servlet.http.HttpSession;
import com.isagulova.spring_eshop.service.SessionObjectHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
public class MainController {

    private final SessionObjectHolder sessionObjectHolder;
    private final UserService userService;

    public MainController(SessionObjectHolder sessionObjectHolder, UserService userService) {
        this.sessionObjectHolder = sessionObjectHolder;
        this.userService = userService;
    }

    @RequestMapping({"", "/"})
    public String index(Model model, HttpSession httpSession) {
        model.addAttribute("amountClicks", sessionObjectHolder.getAmountClicks());
        if (httpSession.getAttribute("myID") == null) {
            String uuid = UUID.randomUUID().toString();
            httpSession.setAttribute("myID", uuid);
        }
        model.addAttribute("uuid", httpSession.getAttribute("myID"));
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "register";
    }


    @PostMapping("/register")
    public String saveUser(UserDTO dto, Model model) {
        if (userService.save(dto)) {
            return "redirect:/notifyPage";
        } else {
            model.addAttribute("user", dto);
            return "register";
        }
    }

    @GetMapping("/notifyPage")
    public String showNotifyPage() {
        return "notifyPage";
    }

    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }
}
