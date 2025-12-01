package kg.Isagulova.spring_eshop.service;

import jakarta.transaction.Transactional;
import kg.Isagulova.spring_eshop.dao.ProductRepository;
import kg.Isagulova.spring_eshop.domain.Bucket;
import kg.Isagulova.spring_eshop.domain.User;
import kg.Isagulova.spring_eshop.dto.ProductDTO;
import kg.Isagulova.spring_eshop.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductMapper mapper = ProductMapper.MAPPER;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final BucketService bucketService;

    public ProductServiceImpl(ProductRepository productRepository, UserService userService, BucketService bucketService) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.bucketService = bucketService;
    }

    @Override
    public List<ProductDTO> getAll(){
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
        }else{
            bucketService.addProducts(bucket, Collections.singletonList(productId));
        }
    }
}
