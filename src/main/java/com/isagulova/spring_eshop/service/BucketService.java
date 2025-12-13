package com.isagulova.spring_eshop.service;

import com.isagulova.spring_eshop.domain.Bucket;
import com.isagulova.spring_eshop.domain.User;
import com.isagulova.spring_eshop.dto.BucketDTO;

import java.util.List;

public interface BucketService {
    Bucket createBucket(User user, List<Long> productIds);

    void addProducts(Bucket bucket, List<Long> productIds);

    BucketDTO getBucketByUser(String name);

    void commitBucketToOrder(String username);
}
