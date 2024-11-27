package com.springboot.jap.data.entity;

import com.springboot.jap.data.dto.ProductDto;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long number;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer stock;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // ddl-auto의 값을 create로 설정 => 쿼리문 생성 안해도 db에 자동으로 테이블 생성
}
