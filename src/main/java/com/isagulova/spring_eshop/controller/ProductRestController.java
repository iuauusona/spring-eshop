//package com.isagulova.spring_eshop.controller;
//
//import com.isagulova.spring_eshop.dto.ProductDTO;
//import com.isagulova.spring_eshop.service.ProductsService;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/v1/products")
//public class ProductRestController {
//
//    private final ProductsService productsService;
//
//
//    public ProductRestController(ProductsService productsService) {
//        this.productsService = productsService;
//    }
//
//    @GetMapping("/{id}")
//    public ProductDTO getById(@PathVariable Long id){
//        return productsService.getById(id);
//    }
//
//    @PostMapping
//    public void addProduct(@RequestBody ProductDTO dto){
//        productsService.addProduct(dto);
//    }
//
//}