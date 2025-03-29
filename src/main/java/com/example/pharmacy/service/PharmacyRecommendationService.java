package com.example.pharmacy.service;

import com.example.pharmacy.api.dto.DocumentDto;
import com.example.pharmacy.api.dto.KakaoApiResponseDto;
import com.example.pharmacy.api.service.KakaoAddressSearchService;
import com.example.pharmacy.cache.PharmacyRedisTemplateService;
import com.example.pharmacy.dto.OutputDto;
import com.example.pharmacy.entity.Direction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PharmacyRecommendationService {

    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final DirectionService directionService;
    private final Base62Service base62Service;
    private final PharmacyRedisTemplateService pharmacyRedisTemplateService;

    private static final String ROAD_VIEW_BASE_URL = "https://map.kakao.com/link/roadview/";    // 로드뷰 URL
    //private static final String DIRECTION_BASE_URL = "https://map.kakao.com/link/map/";

    @Value("${pharmacy.recommendation.base.url}")
    private String baseUrl;

    // 주소 입력 -> 위치 기반 데이터로 변환 -> validation 체크 -> 변환된 위도 경도 기준으로 가까운 약국 찾기
    public List<OutputDto> recommendPharmacyList(String address) {

        List<OutputDto> outputDtos = pharmacyRedisTemplateService.findByAddress(address);
        if(!CollectionUtils.isEmpty(outputDtos)) {
            return outputDtos;
        }
        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);  // 내 위치 데이터를 가져온다 (내가 검색한 주소)

        if (Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.documentList())) {
            log.error("[PharmacyRecommendationService recommendPharmacyList fail] Input address: {}", address);
            return Collections.emptyList();
        }

        DocumentDto documentDto = kakaoApiResponseDto.documentList().get(0);

        //List<Direction> directionList = directionService.buildDirectionList(documentDto);   // DB에서 가까운 약국 리스트 조회
        List<Direction> categoryDirectionList = directionService.buildDirectionListByCategoryApi(documentDto);  // 내 위치 검색 후 -> 근처 약국 조회 -> 필터링해서 반환

        List<OutputDto> outputResult = directionService.saveAll(categoryDirectionList)
                .stream()
                .map(t -> convertToOutputDto(t))
                .collect(Collectors.toList());

        System.out.println("address = " + address);
        pharmacyRedisTemplateService.savePharmacy(address, outputResult);


        return outputResult;
    }

    public OutputDto convertToOutputDto(Direction direction) {  // shortenURL
        return OutputDto.builder()
                .pharmacyName(direction.getTargetPharmacyName())
                .pharmacyAddress(direction.getTargetAddress())
                .directionUrl(baseUrl + base62Service.encodeDirectionId(direction.getId()))
                .roadViewUrl(ROAD_VIEW_BASE_URL + direction.getTargetLatitude() + "," + direction.getTargetLongitude())
                .distance(String.format("%.2f km", direction.getDistance()))
                .build();
    }
}
