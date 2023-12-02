package com.example.pharmacy.api.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class KakaoUriBuilderServiceTest {
    @InjectMocks
    private KakaoUriBuilderService kakaoUriBuilderService;

    @DisplayName("한글 파라미터의 경우 정상적으로 인코딩")
    @Test
    public void buildUriByAddressEncodeTest() throws Exception{
        //given
        String address = "서울 성북구";

        //when
        URI uri = kakaoUriBuilderService.buildUriByAddressSearch(address);

        String decode = URLDecoder.decode(uri.toString(), StandardCharsets.UTF_8);

        //then
        System.out.println("인코딩 된 URI: " + uri);
        System.out.println("디코딩 된 URI: " + decode);

        assertThat(decode).isEqualTo("https://dapi.kakao.com/v2/local/search/address.json?query=서울 성북구");
    }

}