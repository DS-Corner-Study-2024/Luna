package com.springboot.test.data.repository;

import com.springboot.test.data.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ProductRepositoryTestH2 {

    @Autowired
    private ProductRepository productRepository;

    // 저장
    @Test
    void saveTest() {
        //given - Product 엔티티 생서
        Product product = new Product();
        product.setName("펜");
        product.setPrice(1000);
        product.setStock(1000);

        //when - 생성된 엔티티 기반으로 save() 메서드 호출해 테스트 진행
        Product savedProduct = productRepository.save(product);

        //then - 리턴 객체와 Given에서 생성한 엔티티 객체의 값이 일치하는지 검즌
        assertEquals(product.getName(), savedProduct.getName());
        assertEquals(product.getPrice(), savedProduct.getPrice());
        assertEquals(product.getStock(), savedProduct.getStock());
    }

    // 조회
    @Test
    void selectTest() {
        //given
        Product product = new Product();
        product.setName("펜");
        product.setPrice(1000);
        product.setStock(1000);

        // DB에 저장하는 작업 수행
        Product savedProduct = productRepository.saveAndFlush(product);

        //when - 조회 메서드 호출해 테스트 진행
        Product foundProduct = productRepository.findById(savedProduct.getNumber()).get();

        //then - 코드에서 데이터 비교하며 검증
        assertEquals(product.getName(), foundProduct.getName());
        assertEquals(product.getPrice(), foundProduct.getPrice());
        assertEquals(product.getStock(), foundProduct.getStock());
    }
}
