package com.springboot.test.data.repository;


import com.springboot.test.data.entity.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ProductRepositoryTest2 {

    @Autowired
    ProductRepository productRepository;

    @Test
    public void basicCRUDTest() {
        // >> create
        //given - 기본메스드 테스트이므로 한번만 사용해 전체 테스트에 활용함
        Product givenProduct = Product.builder()
                .name("노트")
                .price(1000)
                .stock(500)
                .build();

        // when
        Product savedProduct = productRepository.save(givenProduct);

        //then
        Assertions.assertThat(savedProduct.getNumber()).isEqualTo(givenProduct.getNumber());
        Assertions.assertThat(savedProduct.getName()).isEqualTo(givenProduct.getName());
        Assertions.assertThat(savedProduct.getPrice()).isEqualTo(givenProduct.getPrice());
        Assertions.assertThat(savedProduct.getStock()).isEqualTo(givenProduct.getStock());


        // >> read
        // when
        Product selectProduct = productRepository.findById(savedProduct.getNumber()).orElseThrow(RuntimeException::new);

        // then
        Assertions.assertThat(selectProduct.getNumber()).isEqualTo(givenProduct.getNumber());
        Assertions.assertThat(selectProduct.getName()).isEqualTo(givenProduct.getName());
        Assertions.assertThat(selectProduct.getPrice()).isEqualTo(givenProduct.getPrice());
        Assertions.assertThat(selectProduct.getStock()).isEqualTo(givenProduct.getStock());


        // >> update
        // when
        Product foundProduct = productRepository.findById(selectProduct.getNumber()).orElseThrow(RuntimeException::new);

        foundProduct.setName("장난감");

        Product updatedProduct = productRepository.save(foundProduct);

        // then
        assertEquals(updatedProduct.getName(), "장난감");


        // >> delete
        //when
        productRepository.delete(updatedProduct);

        // then
        assertFalse(productRepository.findById(selectProduct.getNumber()).isPresent());
    }
}
