package com.example.pharmacy.controller;

import com.example.pharmacy.cache.PharmacyRedisTemplateService;
import com.example.pharmacy.dto.PharmacyDto;
import com.example.pharmacy.entity.Pharmacy;
import com.example.pharmacy.service.PharmacyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PharmacyController {

    private final PharmacyService pharmacyService;
    private final PharmacyRedisTemplateService pharmacyRedisTemplateService;

    // 데이터 초기 셋팅을 위한 임시 메서드
    @GetMapping("/redis/save")
    public String save() {

        saveCsvToRedis();

        return "success";
    }

    public void saveCsvToRedis() {

        List<PharmacyDto> pharmacyDtoList = pharmacyService.findAll()
                .stream().map(pharmacy -> PharmacyDto.builder()
                        .id(pharmacy.getId())
                        .pharmacyName(pharmacy.getPharmacyName())
                        .pharmacyAddress(pharmacy.getPharmacyAddress())
                        .latitude(pharmacy.getLatitude())
                        .longitude(pharmacy.getLongitude())
                        .build())
                .collect(Collectors.toList());

        pharmacyDtoList.forEach(pharmacyDto -> pharmacyRedisTemplateService.save(pharmacyDto));
    }
}
