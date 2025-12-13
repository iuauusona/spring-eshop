package com.isagulova.spring_eshop.service;

import com.isagulova.spring_eshop.domain.Product;
import jakarta.transaction.Transactional;
import com.isagulova.spring_eshop.dao.ProductRepository;
import com.isagulova.spring_eshop.domain.Bucket;
import com.isagulova.spring_eshop.domain.User;
import com.isagulova.spring_eshop.dto.ProductDTO;
import com.isagulova.spring_eshop.mapper.ProductMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductsService {
    private final ProductMapper mapper = ProductMapper.MAPPER;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final BucketService bucketService;
    private SimpMessagingTemplate template;

    public ProductServiceImpl(ProductRepository productRepository,
                              UserService userService,
                              BucketService bucketService,
                              SimpMessagingTemplate template) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.bucketService = bucketService;
        this.template = template;
    }

    @Override
    public List<ProductDTO> getAll() {
        return mapper.fromProductList(productRepository.findAll());
    }

    @Override
    @Transactional
    public void addToUserBucket(Long productId, String username) {
        User user = userService.findByName(username);
        if (user == null) {
            throw new RuntimeException("User not found - " + username);
        }
        Bucket bucket = user.getBucket();
        if (bucket == null) {
            Bucket newBucket = bucketService.createBucket(user, Collections.singletonList(productId));
            user.setBucket(newBucket);
            userService.save(user);
        } else {
            bucketService.addProducts(bucket, Collections.singletonList(productId));
        }
    }

    @Override
    @Transactional
    public void addProduct(ProductDTO dto) {
        Product product = mapper.toProduct(dto);
        product.setId(null);
        Product savedProduct = productRepository.save(product);
        template.convertAndSend("/topic/products", ProductMapper.MAPPER.fromProduct(savedProduct));
    }

    @Override
    public ProductDTO getById(Long id) {
        Product product = productRepository.findById(id).orElse(new Product());
        return ProductMapper.MAPPER.fromProduct(product);
    }
}
