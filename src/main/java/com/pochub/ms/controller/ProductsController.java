package com.pochub.ms.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pochub.ms.dto.ProductReq;
import com.pochub.ms.dto.ProductRes;
import com.pochub.ms.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductsController {

    private ProductService productService;
    
    public ProductsController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductRes> fetchAll() {
        return productService.fetchAll();
    }
    
    @GetMapping("{id}")
    public Optional<ProductRes> fetchById(@PathVariable Long id) {
        return productService.fetchById(id);
    }
    
    @PostMapping
    public ProductRes add(@RequestBody ProductReq product) {
        return productService.create(product);
    }
    
    @PutMapping("{id}")
    public ProductRes update(@PathVariable Long id, @RequestBody ProductReq product) {
        return productService.update(id, product);
    }
    
    @DeleteMapping("{id}")
	public void remove(@PathVariable Long id) {
	    productService.remove(id);
	}
}
