package com.isagulova.spring_eshop.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isagulova.spring_eshop.dto.ProductDTO;
import com.isagulova.spring_eshop.service.ProductsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ProductController2IT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductsService productService;

    private ProductDTO expectedProduct;

    @BeforeEach
    void setUp() {
        expectedProduct = new ProductDTO(99L, "Test Product", 999.99);

        given(productService.getById(99L))
                .willReturn(expectedProduct);
    }
    @Test
    @WithMockUser
    void getById() throws Exception {

        mockMvc.perform(get("/products/{id}", 99))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(99))
                .andExpect(jsonPath("$.title").value("Test Product"))
                .andExpect(jsonPath("$.price").value(999.99));
    }
    @Test
    @WithMockUser
    void addProduct() throws Exception {

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedProduct)))
                .andExpect(status().isOk());

        ArgumentCaptor<ProductDTO> captor = ArgumentCaptor.forClass(ProductDTO.class);
        verify(productService).addProduct(captor.capture());

        ProductDTO actual = captor.getValue();
        org.junit.jupiter.api.Assertions.assertEquals(expectedProduct, actual);
    }
}
