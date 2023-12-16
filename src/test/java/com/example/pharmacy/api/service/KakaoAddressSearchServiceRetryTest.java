package com.example.pharmacy.api.service;

import com.example.pharmacy.api.dto.DocumentDto;
import com.example.pharmacy.api.dto.KakaoApiResponseDto;
import com.example.pharmacy.api.dto.MetaDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class KakaoAddressSearchServiceRetryTest {

    @MockBean
    private KakaoUriBuilderService kakaoUriBuilderService;
    @Autowired
    private KakaoAddressSearchService kakaoAddressSearchService;


    private static MockWebServer mockWebServer;

    private final ObjectMapper mapper = new ObjectMapper();

    private final String inputAddress = "서울 성북구 종암로 10길";

    @BeforeEach
    public void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        System.out.println("mockWebServer.getPort() = " + mockWebServer.getPort());
        System.out.println("mockWebServer.url = " + mockWebServer.url("/"));
    }

    @AfterEach
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Disabled
    @DisplayName("재처리 성공")
    @Test
    public void retrySuccessTest() throws Exception{
        //given
        MetaDto metaDto = new MetaDto(1);
        DocumentDto documentDto = DocumentDto.builder()
                .addressName(inputAddress)
                .build();
        KakaoApiResponseDto expectedResponse = new KakaoApiResponseDto(metaDto, Arrays.asList(documentDto));
        URI uri = mockWebServer.url("/").uri();

        //when
        mockWebServer.enqueue(new MockResponse().setResponseCode(504)); // 첫 번째 호출은 504에러 발생
        mockWebServer.enqueue(new MockResponse().setResponseCode(200)   // 두 번째 호출은 정상
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(mapper.writeValueAsString(expectedResponse))); // 실제 카카오api를 받는것처럼 헤더와 바디를 추가

        KakaoApiResponseDto kakaoApiResult = kakaoAddressSearchService.requestAddressSearch(inputAddress);
        RecordedRequest takeRequest = mockWebServer.takeRequest();

        //when(kakaoUriBuilderService.buildUriByAddressSearch(ArgumentMatchers.any())).thenReturn(uri);

        //then
        //2 * kakaoUriBuilderService.buildUriByAddressSearch(inputAddress) >> uri
        assertThat(takeRequest.getMethod()).isEqualTo("GET");
        assertThat(kakaoApiResult.documentList().size()).isEqualTo(1);
        assertThat(kakaoApiResult.metaDto().totalCount()).isEqualTo(1);
        assertThat(kakaoApiResult.documentList().get(0).getAddressName()).isEqualTo(inputAddress);
    }

    @DisplayName("재처리 실패")
    @Test
    public void retryFailTest() throws Exception{
        //given
        URI uri = mockWebServer.url("/").uri();

        //when
        mockWebServer.enqueue(new MockResponse().setResponseCode(504));
        mockWebServer.enqueue(new MockResponse().setResponseCode(504)); // 첫번째 두번째 모두 에러처리 발생

        KakaoApiResponseDto result = kakaoAddressSearchService.requestAddressSearch(inputAddress);
        //when(kakaoUriBuilderService.buildUriByAddressSearch(ArgumentMatchers.any())).thenReturn(uri);

        //then
        //assertThat()
        assertThat(result).isNull();
    }
}