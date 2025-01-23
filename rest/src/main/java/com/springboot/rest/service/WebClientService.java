package com.springboot.rest.service;

import com.springboot.rest.dto.MemberDto;
import org.apache.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WebClientService {

    public String getName() {
        // WebClient -> 객체 생성 후 요청 전달하는 방식으로 동작 => WebClient 객체 이용 시 객체 생성 후 재사용하는 방식으로 구현
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:9090") // 기본 URL 설정
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) // 헤더의 값 설정
                .build();

        return webClient.get() // HTTP 메서드를 get(), post(), put(), delete() 등 네이밍이 명확한 메서드 설정 가능
                .uri("/api/v1/crud-api") // URI확장 방법
                .retrieve() // 요청에 대한 응답을 받았을 떄 그 값을 추출하는 방법
                // Mono : Flux와 비교되는 개념 -> 리액티브 스트림에서 데이터를 제공하는 발행자 역할 수행하는 Publicsher의 구현체
                .bodyToMono(String.class) // 리턴 타입을 설정해 문자열 객체 받아옴
                .block(); // 블로킹 형식으로 동작하게 설정 -> WebClient가 논블로킹 방식으로 동작하기 떄문
    }

    public String getNameWithPathVariable() {
        WebClient webClient = WebClient.create("http://localhost:9090");

        ResponseEntity<String> responseEntity = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/crud-api/{name}")
                        .build("Flature")) // parthVariable 추가하는 방법
                .retrieve().toEntity(String.class).block();

        // 간략하게 작성하는 방법
        ResponseEntity<String> responseEntity1 = webClient.get()
                .uri("/api/v1/crud-api/{name}", "Flature")
                .retrieve()
                .toEntity(String.class) // ResponseEntity 타입으로 응답 전달받음
                .block();

        return responseEntity.getBody();
    }

    public String getNameWithParameter() {
        WebClient webClient = WebClient.create("http://localhost:9090");

        // 쿼리 파라미터를 요청에 담기위해 uriBuilder 사용
        return webClient.get().uri(uriBuilder -> uriBuilder.path("/api/v1/crud-api")
                        .queryParam("name", "Flature") // queryParam() 메서드로 전달하려는 값 설정
                        .build())
                // exchange 지원 중단으로 exchangeToMono나 exchangeTFlux 사용 => 응답 결과 코드에 따라 다르게 응답 설정
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().equals(HttpStatus.OK)) {
                        return clientResponse.bodyToMono(String.class);
                    } else {
                        return clientResponse.createException().flatMap(Mono::error);
                    }
                })
                .block();
    }

    public ResponseEntity<MemberDto> postWithParamAndBody() {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:9090")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        MemberDto memberDTO = new MemberDto();
        memberDTO.setName("flature!!");
        memberDTO.setEmail("flature@gmail.com");
        memberDTO.setOrganization("Around Hub Studio");

        // post() 메서드로 POST 메서드 통신 정의, uri()는 uriBuilder로 path와 parameter 설정
        return webClient.post().uri(uriBuilder -> uriBuilder.path("/api/v1/crud-api")
                        .queryParam("name", "Flature")
                        .queryParam("email", "flature@wikibooks.co.kr")
                        .queryParam("organization", "Wikibooks")
                        .build())
                .bodyValue(memberDTO) // HTTP바디 값을 설정 => 일반적으로 데이터 객체를(DTO, VO) 파라미터로 전달
                .retrieve()
                .toEntity(MemberDto.class)
                .block();
    }

    public ResponseEntity<MemberDto> postWithHeader() {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:9090")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        MemberDto memberDTO = new MemberDto();
        memberDTO.setName("flature!!");
        memberDTO.setEmail("flature@gmail.com");
        memberDTO.setOrganization("Around Hub Studio");

        return webClient
                .post()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/crud-api/add-header")
                        .build())
                .bodyValue(memberDTO)
                .header("my-header", "Wikibooks API") // 헤더에 값 추가 -> 외부 API사용을 위해 인증된 토큰값 담아 전달함
                .retrieve()
                .toEntity(MemberDto.class)
                .block();
    }

    // 빌드된 WebClient는 변경 x 복사 후 사용o
    public void cloneWebClient() {
        WebClient webClient = WebClient.create("http://localhost:9090");

        WebClient clone = webClient.mutate().build();
    }
}