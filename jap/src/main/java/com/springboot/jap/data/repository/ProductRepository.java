package com.springboot.jap.data.repository;

import com.springboot.jap.data.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
