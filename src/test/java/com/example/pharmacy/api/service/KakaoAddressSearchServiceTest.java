package com.example.pharmacy.api.service;

import com.example.pharmacy.api.dto.KakaoApiResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
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

    @DisplayName("정상적인 주소를 입력했을 경우, 정삭적으로 위도 경도로 반환 된다.")
    @Test
    public void inputAddressTest() throws Exception{
        //given
        boolean actualResult = false;
        String address = "서울 성북구 종암로 10길";
        String nullAddress = "";
        //when
        KakaoApiResponseDto searchResult = kakaoAddressSearchService.requestAddressSearch(address);
        KakaoApiResponseDto searchNullResult = kakaoAddressSearchService.requestAddressSearch(nullAddress);

        //then
        assertThat(searchResult.documentList().size()).isGreaterThan(0);
        assertThat(searchNullResult).isNull();
    }
}