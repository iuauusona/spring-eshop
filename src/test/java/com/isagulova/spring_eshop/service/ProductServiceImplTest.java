package com.isagulova.spring_eshop.service;


import com.isagulova.spring_eshop.dao.ProductRepository;
import com.isagulova.spring_eshop.dao.UserRepository;
import com.isagulova.spring_eshop.domain.Product;
import com.isagulova.spring_eshop.dto.ProductDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;


public class ProductServiceImplTest {
    private ProductsService productService;

    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserService userService;
    @Mock
    private BucketService bucketService;
    @Mock
    private SimpMessagingTemplate template;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        productService = new ProductServiceImpl(productRepository, userService, bucketService, template);
    }

    @Test
    void getById() {
        //have
        Product product = Product.builder()
                .id(10L)
                .title("Product")
                .price(10.0)
                .build();

        Mockito.when(productRepository.findById(Mockito.eq(10L))).thenReturn(Optional.of(product));
        //execute
        ProductDTO productDTO = productService.getById(10L);
        //check
        Assertions.assertNotNull(productDTO);
        Assertions.assertEquals(product.getId(), productDTO.getId());
        Assertions.assertEquals(product.getTitle(), productDTO.getTitle());
        Assertions.assertEquals(product.getPrice(), productDTO.getPrice());
    }
}
