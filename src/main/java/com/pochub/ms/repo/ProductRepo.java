package com.pochub.ms.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pochub.ms.entity.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

}
