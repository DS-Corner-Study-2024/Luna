package com.springboot.advanced_jpa.data.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.springboot.advanced_jpa.data.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    void sortingAndPagingTest() {
        productRepository.findByName("펜", Sort.by(Order.asc("price")));
        productRepository.findByName("펜", Sort.by(Order.asc("price"), Order.desc("stock")));

        Product product1 = new Product();
        product1.setName("펜");
        product1.setPrice(1000);
        product1.setStock(100);
        product1.setCreatedAt(LocalDateTime.now());
        product1.setUpdatedAt(LocalDateTime.now());

        Product product2 = new Product();
        product2.setName("펜");
        product2.setPrice(5000);
        product2.setStock(300);
        product2.setCreatedAt(LocalDateTime.now());
        product2.setUpdatedAt(LocalDateTime.now());

        Product product3 = new Product();
        product3.setName("펜");
        product3.setPrice(500);
        product3.setStock(50);
        product3.setCreatedAt(LocalDateTime.now());
        product3.setUpdatedAt(LocalDateTime.now());

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);

        System.out.println(productRepository.findByName("펜", getSort()));

        Page<Product> productPage = productRepository.findByName("펜", PageRequest.of(0, 2));
        System.out.println(productPage.getContent());
    }

    // 정렬 조건을 반환하는 메서드
    private Sort getSort() {
        return Sort.by(
                Order.asc("price"),
                Order.desc("stock")
        );
    }

    @PersistenceContext
    EntityManager entityManager;

    @Test
    void queryDslTest() {
        JPAQuery<Product> query = new JPAQuery(entityManager);
        QProduct qProduct = QProduct.product;

        List<Product> productList = query.from(qProduct).where(qProduct.name.eq("펜")).orderBy(qProduct.price.asc()).fetch();

        for (Product product : productList){
            System.out.println("-------------------");
            System.out.println();
            System.out.println("Product Number: " + product.getNumber());
            System.out.println("Product Name: " + product.getName());
            System.out.println("Product Price: " + product.getPrice());
            System.out.println("Product Stock: " + product.getStock());
            System.out.println();
            System.out.println("-------------------");
        }
    }
}