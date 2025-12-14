package com.isagulova.spring_eshop.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ProductControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void checkGetProductById() throws Exception {
        mockMvc.perform(get("/products/" + 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Test
//    void checkGetProductById() {
//        ResponseEntity<ProductDTO> entity = restTemplate.getForEntity("/products/" + 1, ProductDTO.class);
//        Assertions.assertEquals(HttpStatus.OK, entity.getStatusCode());
//    }

}