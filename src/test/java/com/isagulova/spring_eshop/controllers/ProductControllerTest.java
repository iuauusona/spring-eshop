package com.isagulova.spring_eshop.controllers;

import com.isagulova.spring_eshop.controller.ProductController;
import com.isagulova.spring_eshop.dto.ProductDTO;
import com.isagulova.spring_eshop.service.ProductsService;
import com.isagulova.spring_eshop.service.SessionObjectHolder;
import com.isagulova.spring_eshop.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductsService productService;
    @MockBean
    private UserService userService;
    @MockBean
    private SessionObjectHolder sessionObjectHolder;

    private ProductDTO dto1 = new ProductDTO(998L, "TestProduct998", 888.88);
    private ProductDTO dto2 = new ProductDTO(999L, "TestProduct999", 999.99);

    @BeforeEach
    void setUp() {
        given(productService.getAll()).willReturn(Arrays.asList(dto1, dto2));
    }

    @Test
    @WithMockUser
    void checkList() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/products")
                                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers
                        .content().string(Matchers.containsString("<td>" + dto1.getTitle() + "</td>")))
                .andExpect(MockMvcResultMatchers
                        .content().string(Matchers.containsString("<td>" + dto2.getTitle() + "</td>")));

    }
}