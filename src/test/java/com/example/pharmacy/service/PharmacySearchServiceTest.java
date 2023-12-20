package com.example.pharmacy.service;

import com.example.pharmacy.cache.PharmacyRedisTemplateService;
import com.example.pharmacy.dto.PharmacyDto;
import com.example.pharmacy.entity.Pharmacy;
import org.apache.commons.compress.utils.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class PharmacySearchServiceTest {

    @MockBean
    private PharmacyService pharmacyService;
    @MockBean
    private PharmacyRedisTemplateService pharmacyRedisTemplateService;

    @Autowired
    private PharmacySearchService pharmacySearchService;

    @DisplayName("레디스 장애시 DB를 이용하여 약국 데이터 조회")
    @Test
    public void DBPharmacyDataTest() throws Exception{
        //given
        ArrayList<Pharmacy> pharmacyList = Lists.newArrayList();

        pharmacyList.add(
                Pharmacy.builder()
                        .id(1L)
                        .pharmacyName("호수온누리약국")
                        .latitude(37.60894032)
                        .longitude(127.029052)
                        .build()
        );

        pharmacyList.add(
                Pharmacy.builder()
                        .id(2L)
                        .pharmacyName("돌곶이온누리약국")
                        .latitude(37.61040424)
                        .longitude(127.0569046)
                        .build()
        );

        //when
        when(pharmacyRedisTemplateService.findAll()).thenReturn(Collections.emptyList());
        when(pharmacyService.findAll()).thenReturn(pharmacyList);

        List<PharmacyDto> pharmacyDtos = pharmacySearchService.searchPharmacyDtoList();

        //then
        assertThat(pharmacyDtos.size()).isEqualTo(2);
    }
}