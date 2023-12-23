package com.example.pharmacy.service;

import com.example.pharmacy.api.dto.DocumentDto;
import com.example.pharmacy.api.service.KakaoCategorySearchService;
import com.example.pharmacy.dto.PharmacyDto;
import com.example.pharmacy.entity.Direction;
import com.example.pharmacy.repository.DirectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

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
    private static final String DIRECTION_BASE_URL = "https://map.kakao.com/link/map/";

    private final PharmacySearchService pharmacySearchService;
    private final DirectionRepository directionRepository;
    private final KakaoCategorySearchService kakaoCategorySearchService;
    private final Base62Service base62Service;

    @Transactional
    public List<Direction> saveAll(List<Direction> directionList) {
        if(CollectionUtils.isEmpty(directionList)) return Collections.emptyList();
        return directionRepository.saveAll(directionList);
    }

    public String findDirectionUrlById(String encodedId) {
        Long decodedId = base62Service.decodeDirectionId(encodedId);
        Direction direction = directionRepository.findById(decodedId).orElse(null);

        String params = String.join(",", direction.getTargetPharmacyName(),
                String.valueOf(direction.getTargetLatitude()), String.valueOf(direction.getTargetLongitude()));
        // ex: 은혜약국,38.11,128.11

        String result = UriComponentsBuilder.fromHttpUrl(DIRECTION_BASE_URL + params)
                .toUriString();

        log.info("direction params: {}, url: {}", params, result);

        return result;
    }

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

    public List<Direction> buildDirectionListByCategoryApi(DocumentDto inputDocumentDto) {
        if(Objects.isNull(inputDocumentDto)) return Collections.emptyList();

        return kakaoCategorySearchService
                .requestPharmacyCategorySearch(inputDocumentDto.getLatitude(), inputDocumentDto.getLongitude(), RADIUS_KM)
                .documentList()
                .stream().map(resultDocumentDto ->
                        Direction.builder()
                                .inputAddress(inputDocumentDto.getAddressName())
                                .inputLatitude(inputDocumentDto.getLatitude())
                                .inputLongitude(inputDocumentDto.getLongitude())
                                .targetPharmacyName(resultDocumentDto.getPlaceName())
                                .targetAddress(resultDocumentDto.getAddressName())
                                .targetLatitude(resultDocumentDto.getLatitude())
                                .targetLongitude(resultDocumentDto.getLongitude())
                                .distance(resultDocumentDto.getDistance() * 0.001) // km 단위
                                .build())
                .limit(MAX_SEARCH_COUNT)
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
