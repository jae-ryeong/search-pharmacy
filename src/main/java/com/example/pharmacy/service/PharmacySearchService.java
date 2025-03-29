package com.example.pharmacy.service;

import com.example.pharmacy.api.dto.KakaoApiResponseDto;
import com.example.pharmacy.api.service.KakaoUriBuilderService;
import com.example.pharmacy.cache.PharmacyRedisTemplateService;
import com.example.pharmacy.dto.PharmacyDto;
import com.example.pharmacy.entity.Pharmacy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacySearchService { // -> 약국 데이터 조회 -> 약국dto에 전달

    private final PharmacyService pharmacyService;
    private final PharmacyRedisTemplateService pharmacyRedisTemplateService;
    private final KakaoUriBuilderService kakaoUriBuilderService;
    private final RestTemplate restTemplate;
    private static final String PHARMACY_CATEGORY = "PM9";

    public List<PharmacyDto> searchPharmacyDtoList() {  // redis에서 먼저 조회 후 문제 발생시 db에서 조회
        // redis
        List<PharmacyDto> pharmacyDtoList = pharmacyRedisTemplateService.findAll();
        if(!pharmacyDtoList.isEmpty()) {
            log.info("redis findAll success!");
            return pharmacyDtoList;
        }

        // db
        return pharmacyService.findAll().stream()
                .map(this::convertToPharmacyDto)
                .collect(Collectors.toList());
    }

    @Value("${kakao.rest.api.key}")
    private String kakaoRestApiKey;

    public KakaoApiResponseDto requestPharmacyCategorySearch(double latitude, double longitude, double radius) {
        URI uri = kakaoUriBuilderService.buildUriByCategorySearch(latitude, longitude, radius, PHARMACY_CATEGORY);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoRestApiKey);
        HttpEntity httpEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(uri, HttpMethod.GET, httpEntity, KakaoApiResponseDto.class).getBody();
    }


    private PharmacyDto convertToPharmacyDto(Pharmacy pharmacy) {   // 엔티티를 dto로 변환
        return PharmacyDto.builder()
                .id(pharmacy.getId())
                .pharmacyAddress(pharmacy.getPharmacyAddress())
                .pharmacyName(pharmacy.getPharmacyName())
                .latitude(pharmacy.getLatitude())
                .longitude(pharmacy.getLongitude())
                .build();
    }
}
