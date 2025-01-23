package com.springboot.rest.service;

import com.springboot.rest.dto.MemberDto;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Member;
import java.net.URI;

@Service
public class RestTemplateService {
    public RestTemplate restTemplate(){
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

        HttpClient client = HttpClientBuilder.create()
                .setMaxConnTotal(500)
                .setMaxConnPerRoute(500)
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setMaxConnTotal(500)
                .setMaxConnPerRoute(500)
                .build();

        // HttpClient 생성하면 setHttpClient()통해 인자 전달해 설정 가능
        factory.setHttpClient(httpClient);
        factory.setConnectTimeout(2000);
        factory.setReadTimeout(5000);

        RestTemplate restTemplate = new RestTemplate(factory);

        return restTemplate;
    }

    // GET 부분
    // PathVariable or 파라미터 사용하지 않는 호출 및 getForEntity으 파라미터로 전달
    public String getName(){
        // uri-> restTemplate이 외부 API 요청하는데 사용 및
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090") // 호출부의 URL 입력
                .path("/api/v1/crud-api") // 세부 경로 입력
                .encode() // 인코딩 문자셋 설정 -> 기본값(StandardCharsets.UTF_8)
                .build() // 빌더 생성 종류 및 UriComponents 타입 리턴
                .toUri(); // URI타입으로 리턴, String 타입 리턴=> toUriString() 사용

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class); // URI와 응답받는 타입을 매개변수로 사용


        return responseEntity.getBody();
    }

    public String getNameWithPathVariable(){
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/v1/crud-api/{name}") // 중괄호에 변수명 입력
                .encode()
                .build()
                .expand("Flature") // path의 변수명에 해당하는 값을 순서대로 입력, 복수의 값을 넣어야할 경우 ',' 추가하여 구분
                .toUri();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

        return responseEntity.getBody();
    }

    public String getNameWithParameter(){
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/v1/crud-api/param")
                .queryParam("name", "Flature") // (키, 값) 형식으로 파라미터 추가
                .encode()
                .build()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

        return responseEntity.getBody();
    }

    // POST 부분 -> 오부 API 요청할 떄 Body 값과 파라미터 값을 담는 방법
    public ResponseEntity<MemberDto> postWithParamAndBody(){
        // 파라미터에 값 추가하는 방법
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/v1/crud-api")
                .queryParam("name", "Flature")
                .queryParam("email", "flature@wikibooks.co.kr")
                .queryParam("organization", "Wikibooks")
                .encode()
                .build()
                .toUri();

        // RequestBody에 값을 담는 방법 -> 데이터 객체 생성 후 값을 담음
        MemberDto memberDto = new MemberDto();
        memberDto.setName("flature!!");
        memberDto.setEmail("flature@gmail.com");
        memberDto.setOrganization("Around Hub Studio");

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<MemberDto> responseEntity = restTemplate.postForEntity(uri, memberDto, MemberDto.class); // 파라미터로 데이터 객처 넣기

        // postForEntity() 메서드로 API 호출 -> 서버 프로젝트 콘솔 로그에 RequsetBody 값 출력 및 파라미터 값이 결괏값으로 리턴
        return responseEntity;
    }

    // 헤더 추가 -> 대부분 외부 API의 토큰값을 헤더에 담아 전달하는 방식
    public ResponseEntity<MemberDto> postWithHeader(){
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/v1/crud-api/add-header")
                .encode()
                .build()
                .toUri();

        MemberDto memberDto = new MemberDto();
        memberDto.setName("flature!!");
        memberDto.setEmail("flature@gmail.com");
        memberDto.setOrganization("Around Hub Studio");

        // RequestEntity -> 헤더 설정하기에 가장 편한 방법
        RequestEntity<MemberDto> requestEntity = RequestEntity
                .post(uri) // uri 설정
                .header("my-header", "Wikibooks API") // 헤더의 키 이름과 값을 설정하는 코드 -> API 명세서에 헤더에 필요한 키 값 요구하면 제시함
                .body(memberDto);

        RestTemplate restTemplate = new RestTemplate();
        // 모든 형식의 HTTP 요청 생성 가능 => RequestEntity 설정을 post가 아닌 다른 형식의 메서드 정의만으로도 쉽게 사용 가능
        ResponseEntity<MemberDto> responseEntity = restTemplate.exchange(requestEntity, MemberDto.class);

        return responseEntity;
    }
}
