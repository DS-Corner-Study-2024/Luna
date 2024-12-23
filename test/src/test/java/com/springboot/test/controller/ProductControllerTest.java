package com.springboot.test.controller;

import com.google.gson.Gson;
import com.springboot.test.data.dto.ProductDto;
import com.springboot.test.data.dto.ProductResponseDto;
import com.springboot.test.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.awt.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest  {

    @Autowired
    // MockMvc : 컨트롤러의 구동 없이 가상의 MVC 환경에서 모의 HTTP 서블릿을 요청하는 유틸리티 클래스
    private MockMvc mockMvc;

    @MockBean
    ProductServiceImpl productService;

    @Test
    @DisplayName("MockMvc를 통한 Product 데이터 가져오기 테스트")
    void getProductTest() throws Exception {
        // Given 부분
        // given() : 객체에 호출되는 메서드와 주입되는 파라미터을 가정함, willReturn() : 리턴할 결과 정의
        given(productService.getProduct(123L)).willReturn(
                new ProductResponseDto(123L, "pen", 5000, 2000)
        );

        String productId = "123";

        // When-Then 부분
        /*
            perform() : 서버로 URL 요청을 보낸 것처럼 통신 테스트 코드를 작성해서 컨트롤러를 테스트할 수 있음
            => MockMcvRequestBuilders에서 제공하는 GET, POST, PUT, DELETE로 URL을 정의해 사용
        */
        mockMvc.perform(
                get("/product?number=" + productId)).andExpect(status().isOk())
                        // andExpect() : perform() 메서드의 결괏값으로 리턴되는 ResultActions 객체를 검증함
                        .andExpect(jsonPath("$.number").exists())
                        .andExpect(jsonPath("$.price").exists())
                        .andExpect(jsonPath("$.stock").exists())
                        // andDO() : 요청과 응답의 전체 내용 확인
                        .andDo(print());

        // verify() : 지정된 메서드가 실행됐는지 검증하는 역할로 given()에 정의된 동작과 대응함
        verify(productService).getProduct(123L);
    }

    @Test
    @DisplayName("Product 데이터 생성 테스트")
    void createProductTest() throws Exception {
        // Mock 객체에서 특정 메서드가 실행되는 경우 실제 Return을 줄 수 없기 때문에 아래와 같이 가정 사항을 만들어줌
        given(productService.saveProduct(new ProductDto("pen", 5000, 2000))).willReturn(
                new ProductResponseDto(12315L, "pen", 5000, 2000)
        );

        ProductDto productDto = ProductDto.builder().name("pen").price(5000).stock(2000).build();

        Gson gson = new Gson();
        String content = gson.toJson(productDto);

        mockMvc.perform(
                        // post() : 리소스 생성 기능을 테스트하기 위해 URL 구성, content() : DTO의 값을 담아 테스트 진행
                        post("/product").content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // jsonPath().exists() : POST 요청을 통해 도출된 결괏값에 대한 각 항목이 존재하는지 검증 -> 대응값이 없으면 오류 발생
                .andExpect(jsonPath("$.number").exists())
                .andExpect(jsonPath("$.price").exists())
                .andExpect(jsonPath("$.stock").exists())
                .andDo(print());

        verify(productService).saveProduct(new ProductDto("pen", 5000, 2000));
    }
}
