package com.springboot.test.service.impl;

import com.springboot.test.data.dto.ProductDto;
import com.springboot.test.data.dto.ProductResponseDto;
import com.springboot.test.data.entity.Product;
import com.springboot.test.data.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class ProductServiceTest {

    // mock() : Mock객체로 ProductRepository 주입받음
    private ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private  ProductServiceImpl productService;

    @BeforeEach
    public void setUpTest(){
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    void getProductTest(){
        // Given 부분
        // 엔티티 객체 생성 및 결괏값 리턴 설정
        Product givenProduct = new Product();
        givenProduct.setNumber(123L);
        givenProduct.setName("펜");
        givenProduct.setPrice(1000);
        givenProduct.setStock(1234);

        Mockito.when(productRepository.findById(123L)).thenReturn(Optional.of(givenProduct));

        // ProductService의 getProduct() 메서드 호출해 동작 테스트
        ProductResponseDto productResponseDto = productService.getProduct(123L);

        // Assertion으로 리턴받은 ProductResponseDto 객체 검증
        Assertions.assertEquals(productResponseDto.getNumber(), givenProduct.getNumber());
        Assertions.assertEquals(productResponseDto.getName(), givenProduct.getName());
        Assertions.assertEquals(productResponseDto.getPrice(), givenProduct.getPrice());
        Assertions.assertEquals(productResponseDto.getStock(), givenProduct.getStock());

        // 검증 보완을 위해 부가 검증 시도
        verify(productRepository).findById(123L);
    }

    @Test
    void saveProductTest(){
        /* any() : Mock 객체의 동작을 정의하거나 검증하는 단계에서 메서드의 실행만 확인 or 클래스 객체를 매개변수로 전달받는 등의 상황에 사용
           -> given()으로 정의된 Mock객체의 메서드 동작 감지는 매개변수 비교 or 레퍼런스 변수의 비교는 주솟값으로 이루어져
           any()로 클래스만 정의하는 경우 존재
         */
        Mockito.when(productRepository.save(any(Product.class))).then(returnsFirstArg());

        ProductResponseDto productResponseDto = productService.saveProduct(new ProductDto("펜", 1000, 1234));

        Assertions.assertEquals(productResponseDto.getName(), "펜");
        Assertions.assertEquals(productResponseDto.getPrice(), 1000);
        Assertions.assertEquals(productResponseDto.getStock(), 1234);

        verify(productRepository).save(any());
    }
}
