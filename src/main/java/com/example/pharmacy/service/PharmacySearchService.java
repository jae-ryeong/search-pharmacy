package com.example.pharmacy.service;

import com.example.pharmacy.dto.PharmacyDto;
import com.example.pharmacy.entity.Pharmacy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacySearchService { // -> 약국 데이터 조회 -> 약국dto에 전달

    private final PharmacyService pharmacyService;

    public List<PharmacyDto> searchPharmacyDtoList() {

        // redis


        // db
        return pharmacyService.findAll().stream()
                .map(this::convertToPharmacyDto)
                .collect(Collectors.toList());
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
