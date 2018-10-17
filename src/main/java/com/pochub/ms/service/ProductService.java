package com.pochub.ms.service;

import static java.util.stream.Collectors.toList;
import static org.springframework.beans.BeanUtils.copyProperties;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pochub.ms.dto.ProductReq;
import com.pochub.ms.dto.ProductRes;
import com.pochub.ms.entity.Product;
import com.pochub.ms.repo.ProductRepo;
import com.pochub.ms.transformer.ProductTransformer;

@Service
public class ProductService {

    private ProductRepo productRepo;
    private ProductTransformer productTransformer;

    public ProductService(ProductRepo productRepo, ProductTransformer productTransformer) {
        this.productRepo = productRepo;
        this.productTransformer = productTransformer;
    }

    public List<ProductRes> fetchAll() {
        return productRepo.findAll().stream().map(productTransformer::toModel).collect(toList());
    }

    public Optional<ProductRes> fetchById(Long id) {
        return productRepo.findById(id).map(productTransformer::toModel);
    }

    public ProductRes create(ProductReq productReq) {
        return productTransformer.toModel(productRepo.save(productTransformer.toEntity(productReq)));
    }

    public ProductRes update(Long id, ProductReq productReq) {
        Product product = productRepo.findById(id).get();
        copyProperties(productReq, product);
        return productTransformer.toModel(product);
    }

    public void remove(Long id) {
        productRepo.deleteById(id);
    }

}
