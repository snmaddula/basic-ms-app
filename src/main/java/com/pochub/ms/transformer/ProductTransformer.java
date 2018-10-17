package com.pochub.ms.transformer;

import static org.springframework.beans.BeanUtils.copyProperties;

import org.springframework.stereotype.Component;

import com.pochub.ms.dto.ProductReq;
import com.pochub.ms.dto.ProductRes;
import com.pochub.ms.entity.Product;

@Component
public class ProductTransformer {

    public Product toEntity(ProductReq productReq) {
        Product product = new Product();
        copyProperties(productReq, product);
        return product;
    }

    public Product toEntity(ProductRes productRes) {
        Product product = new Product();
        copyProperties(productRes, product);
        return product;
    }

    public ProductRes toModel(Product product) {
        ProductRes productRes = new ProductRes();
        copyProperties(product, productRes);
        return productRes;
    }
}
