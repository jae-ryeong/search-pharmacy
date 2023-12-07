package com.example.pharmacy.service;

import com.example.pharmacy.api.dto.DocumentDto;
import com.example.pharmacy.dto.PharmacyDto;
import com.example.pharmacy.entity.Direction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectionService {
    /**
     * 약국 최대 검색 갯수: 3
     * 반경은 10Km
     */
    private static final int MAX_SEARCH_COUNT = 3;
    private static final double RADIUS_KM = 10.0;


    private final PharmacySearchService pharmacySearchService;

    public List<Direction> buildDirectionList(DocumentDto documentDto) { // 약국 최대 3개 까지 추천
        // 약국 데이터 조회 후 거리계산 알고리즘을 이용하여, 고객과 약국 사이의 거리를 계산하고 sort

        if(Objects.isNull(documentDto)) return Collections.emptyList();

        return pharmacySearchService.searchPharmacyDtoList()
                .stream().map(pharmacyDto ->
                        Direction.builder()
                                .inputAddress(documentDto.getAddressName())
                                .inputLatitude(documentDto.getLatitude())
                                .inputLongitude(documentDto.getLongitude())
                                .targetPharmacyName(pharmacyDto.pharmacyName())
                                .targetAddress(pharmacyDto.pharmacyAddress())
                                .targetLatitude(pharmacyDto.latitude())
                                .targetLongitude(pharmacyDto.longitude())
                                .distance(calculateDistance(documentDto.getLatitude(), documentDto.getLongitude(), pharmacyDto.latitude(), pharmacyDto.longitude()))
                                .build())
                .filter(direction -> direction.getDistance() <= RADIUS_KM)  // 필터로 10km 이내 약국만
                .sorted(Comparator.comparing(Direction::getDistance))   // 거리를 기준으로 오름차순
                .limit(MAX_SEARCH_COUNT) // 최대 3개까지만
                .collect(Collectors.toList());


    }

    // Haversine formula
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);    // 고객의 위도
        lon1 = Math.toRadians(lon1);    // 고객의 경도
        lat2 = Math.toRadians(lat2);    // 약국의 위도
        lon2 = Math.toRadians(lon2);    // 약국의 경도

        final double earthRadius = 6371; // Kilometers
        return earthRadius * Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
    }
}
