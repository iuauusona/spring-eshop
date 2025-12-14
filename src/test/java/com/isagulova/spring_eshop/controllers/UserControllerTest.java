package com.isagulova.spring_eshop.controllers;


import com.isagulova.spring_eshop.controller.UserController;
import com.isagulova.spring_eshop.domain.Role;
import com.isagulova.spring_eshop.domain.User;
import com.isagulova.spring_eshop.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserController userController;

    private User clientUser = User.builder()
            .name("testuser").id(1L).role(Role.CLIENT).build();
    @Autowired
    private UserService userService;

    @BeforeEach
    public void setup() {
        Mockito.when(userService.findByName(Mockito.eq(clientUser.getName())))
                .thenReturn(clientUser);
    }

    @Test
    void getRolesNotAuthorized() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/users/testuser/roles")
                                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @WithMockUser("testuser")
    void getRoles() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/users/testuser/roles")
                                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("CLIENT"));
    }

//    @Test
//    @WithMockUser("otheruser")
//    void getRolesWrongUser() throws Exception {
//        mockMvc.perform(
//                        MockMvcRequestBuilders.get("/users/testuser/roles")
//                                .contentType(MediaType.TEXT_HTML))
//                .andExpect(status().is5xxServerError());
//    }

//    @Test
//    void newUserNotAuthorized() throws Exception {
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post("/users/new")
//                                .contentType(MediaType.TEXT_HTML))
//                .andExpect(status().is5xxServerError());
//    }
//
//    @Test
//    @WithMockUser(username = "testuser", authorities = {"CLIENT"})
//    void newUserNotAdmin() throws Exception {
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post("/users/new")
//                                .contentType(MediaType.TEXT_HTML))
//                .andExpect(status().is5xxServerError());
//    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void newUser() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users/new")
                                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }

}
