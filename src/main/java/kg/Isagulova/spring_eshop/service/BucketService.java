package kg.Isagulova.spring_eshop.service;

import kg.Isagulova.spring_eshop.domain.Bucket;
import kg.Isagulova.spring_eshop.domain.User;
import kg.Isagulova.spring_eshop.dto.BucketDTO;

import java.security.Principal;
import java.util.List;

public interface BucketService {
    Bucket createBucket(User user, List<Long> productIds);

    void addProducts(Bucket bucket, List<Long> productIds);
    BucketDTO getBucketByUser(String name);
}
