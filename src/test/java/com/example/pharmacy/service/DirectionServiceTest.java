package com.example.pharmacy.service;

import com.example.pharmacy.api.dto.DocumentDto;
import com.example.pharmacy.dto.PharmacyDto;
import com.example.pharmacy.entity.Direction;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DirectionServiceTest {

    @Mock
    private PharmacySearchService pharmacySearchService;

    @InjectMocks
    private DirectionService directionService;

    @DisplayName("결과 값이 거리 순으로 정렬이 되는지 확인")
    @Test
    public void directionSortTest() throws Exception{
        //given
        String addressName = "서울 성북구 종암로10길";
        double inputLatitude = 37.5960650456809;
        double inputLongitude = 127.037033003036;

        DocumentDto documentDto = DocumentDto.builder()
                .addressName(addressName)
                .latitude(inputLatitude)
                .longitude(inputLongitude)
                .build();

        List<PharmacyDto> pharmacyList = new ArrayList<>();
        pharmacyList.add(
                PharmacyDto.builder()
                        .id(1L)
                        .pharmacyName("돌곶이온누리약국")
                        .pharmacyAddress("주소1")
                        .latitude(37.61040424)
                        .longitude(127.0569046)
                        .build()
        );
        pharmacyList.add(
                PharmacyDto.builder()
                        .id(2L)
                        .pharmacyName("호수온누리약국")
                        .pharmacyAddress("주소2")
                        .latitude(37.60894036)
                        .longitude(127.029052)
                        .build()
        );

        //when
        when(pharmacySearchService.searchPharmacyDtoList()).thenReturn(pharmacyList);

        List<Direction> result = directionService.buildDirectionList(documentDto);

        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getTargetPharmacyName()).isEqualTo("호수온누리약국");
        assertThat(result.get(1).getTargetPharmacyName()).isEqualTo("돌곶이온누리약국");
    }

    @DisplayName("반경 10km 내에만 검색이 되는지 확인")
    @Test
    public void radius10KmTest() throws Exception{
        //given
        List<PharmacyDto> pharmacyList = new ArrayList<>();
        pharmacyList.add(
                PharmacyDto.builder()
                        .id(1L)
                        .pharmacyName("돌곶이온누리약국")
                        .pharmacyAddress("주소1")
                        .latitude(37.61040424)
                        .longitude(127.0569046)
                        .build()
        );
        pharmacyList.add(
                PharmacyDto.builder()
                        .id(2L)
                        .pharmacyName("호수온누리약국")
                        .pharmacyAddress("주소2")
                        .latitude(37.60894036)
                        .longitude(127.029052)
                        .build()
        );
        pharmacyList.add(
                PharmacyDto.builder()
                        .id(3L)
                        .pharmacyName("경기약국")
                        .pharmacyAddress("주소3")
                        .latitude(37.3825107393401)
                        .longitude(127.236707811313)
                        .build()
        );

        String addressName = "서울 성북구 종암로10길";
        double inputLatitude = 37.5960650456809;
        double inputLongitude = 127.037033003036;

        DocumentDto documentDto = DocumentDto.builder()
                .addressName(addressName)
                .latitude(inputLatitude)
                .longitude(inputLongitude)
                .build();
        //when
        when(pharmacySearchService.searchPharmacyDtoList()).thenReturn(pharmacyList);

        List<Direction> result = directionService.buildDirectionList(documentDto);

        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getTargetPharmacyName()).isEqualTo("호수온누리약국");
        assertThat(result.get(1).getTargetPharmacyName()).isEqualTo("돌곶이온누리약국");
    }
}