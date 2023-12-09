package com.example.pharmacy.service;

import com.example.pharmacy.api.dto.DocumentDto;
import com.example.pharmacy.api.dto.KakaoApiResponseDto;
import com.example.pharmacy.api.service.KakaoAddressSearchService;
import com.example.pharmacy.entity.Direction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class PharmacyRecommendationService {

    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final DirectionService directionService;

    // 주소 입력 -> 위치 기반 데이터로 변환 -> validation 체크 -> 변환된 위도 경도 기준으로 가까운 약국 찾기
    public void recommendPharmacyList(String address) {

        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

        if (Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.documentList())) {
            log.error("[PharmacyRecommendationService recommendPharmacyList fail] Input address: {}", address);
            return;
        }

        DocumentDto documentDto = kakaoApiResponseDto.documentList().get(0);

        List<Direction> directionList = directionService.buildDirectionList(documentDto);   // 가까운 약국 리스트(순서대로)
        List<Direction> categoryDirectionList = directionService.buildDirectionListByCategoryApi(documentDto);

        directionService.saveAll(directionList);
    }
}
