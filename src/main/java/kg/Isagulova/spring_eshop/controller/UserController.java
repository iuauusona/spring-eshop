package kg.Isagulova.spring_eshop.controller;


import kg.Isagulova.spring_eshop.dto.UserDTO;
import kg.Isagulova.spring_eshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
