package com.example.pharmacy.cache;

import com.example.pharmacy.dto.OutputDto;
import com.example.pharmacy.dto.PharmacyDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacyRedisTemplateService {

    private static final String CACHE_KEY = "PHARMACY";

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private HashOperations<String, String, String> hashOperations;  // <캐시 키, 서브 키, 벨류> 서브 키: pharmacy의 pk 값, 벨류: 약국 데이터를 string으로(json 형태로) 저장

    @PostConstruct
    public void init() {
        this.hashOperations = redisTemplate.opsForHash();
    }

    public void save(PharmacyDto pharmacyDto) {
        System.out.println("hashOperations = " + hashOperations);
        if (Objects.isNull(pharmacyDto) || Objects.isNull(pharmacyDto.id())) {
            log.error("Required Values muse not be null");
            return;
        }

        try {
            hashOperations.put(CACHE_KEY,
                    pharmacyDto.id().toString(),
                    serializePharmacyDto(pharmacyDto)); // JSON으로 변환
            log.info("[PharmacyRedisTemplateService save success] id: {}", pharmacyDto.id());
        } catch (Exception e) {
            log.error("[PharmacyRedisTemplateService save error] {}", e.getMessage());
        }
    }

    public List<PharmacyDto> findAll() {
        try {
            List<PharmacyDto> list = new ArrayList<>();
            for (String value : hashOperations.entries(CACHE_KEY).values()) {   // JSON -> DTO
                PharmacyDto pharmacyDto = deserializePharmacyDto(value);
                list.add(pharmacyDto);
            }
            return list;
        } catch (Exception e) {
            log.error("PharmacyRedisTemplateService findAll error]: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public void delete(Long id) {
        hashOperations.delete(CACHE_KEY, String.valueOf(id));
        log.info("[PharmacyRedisTemplateService delete]: {} ", id);
    }

    public void savePharmacy(String address, List<OutputDto> outputDto) {
        try {
            String serializeOutputDto = serializeOutputDto(outputDto);
            redisTemplate.opsForValue().set(address, serializeOutputDto, 60, TimeUnit.MINUTES); // 거리 가까운 3군데 1시간동안 redis 저장
        } catch (Exception e) {
            log.error("[PharmacyRedisTemplateService savePharmacy error] {}", e.getMessage());
        }
    }

    public List<OutputDto> findByAddress(String address) {
        String result = (String) redisTemplate.opsForValue().get(address);
        System.out.println("result = " + result);

        try{
            List<OutputDto> outputDtos = objectMapper.readValue(result, new TypeReference<List<OutputDto>>() {
            });
            return outputDtos;
        }catch (Exception e){
            log.error("[PharmacyRedisTemplateService findByAddress error] {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private String serializePharmacyDto(PharmacyDto pharmacyDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(pharmacyDto);    // json형태로 serialize
    }

    private String serializeOutputDto(List<OutputDto> outputDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(outputDto);
    }

    private PharmacyDto deserializePharmacyDto(String value) throws JsonProcessingException {  // json을 dto로 deserialize
        return objectMapper.readValue(value, PharmacyDto.class);
    }
}
