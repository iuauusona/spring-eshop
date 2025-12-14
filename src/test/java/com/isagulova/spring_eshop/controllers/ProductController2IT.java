package com.isagulova.spring_eshop.controllers;


import com.isagulova.spring_eshop.dto.ProductDTO;
import com.isagulova.spring_eshop.service.ProductsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductController2IT {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private ProductsService productService;

    private ProductDTO expectedProduct = new ProductDTO(99L, "Test Product", 999.99);

    @BeforeEach
    void setUp() {
        given(productService.getById(expectedProduct.getId()))
                .willReturn(expectedProduct);
    }

    @Test
    void getById() {
        //execute
        ResponseEntity<ProductDTO> entity =
                restTemplate
                        .getForEntity("/products/" + expectedProduct.getId(), ProductDTO.class);
        //check
        Assertions.assertEquals(HttpStatus.OK, entity.getStatusCode());

        ProductDTO actualProduct = entity.getBody();
        Assertions.assertEquals(expectedProduct, actualProduct);


    }
}