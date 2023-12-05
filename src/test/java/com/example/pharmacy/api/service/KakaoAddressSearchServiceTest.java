package com.example.pharmacy.api.service;

import com.example.pharmacy.api.dto.KakaoApiResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // 통합테스트 이기 때문에 각자 테스트 실행하면 실패한다. (API키 환경 변수 없어서 그런듯)
class KakaoAddressSearchServiceTest {
    @Autowired
    private KakaoAddressSearchService kakaoAddressSearchService;

    @BeforeEach
    void setUp() {
    }

    @DisplayName("address가 null 값이면, requestAddressSearch 메소드는 null을 리턴한다.")
    @Test
    public void addressNullTest() throws Exception{
        //given
        String address = null;

        //when
        KakaoApiResponseDto result = kakaoAddressSearchService.requestAddressSearch(address);

        //then
        assertThat(result).isNull();
    }

    @DisplayName("address가 null이 아니면, requestAddressSearch 메소드가 정상적으로 document를 반환")
    @Test
    public void addressNotNullTest() throws Exception{
        //given
        String address = "서울 성북구 종암로 10길";

        //when
        KakaoApiResponseDto result = kakaoAddressSearchService.requestAddressSearch(address);

        //then
        assertThat(result.documentList().size()).isGreaterThan(0);
        assertThat(result.metaDto().totalCount()).isGreaterThan(0);
        assertThat(result.documentList().get(0).getAddressName()).isNotNull();
    }
}