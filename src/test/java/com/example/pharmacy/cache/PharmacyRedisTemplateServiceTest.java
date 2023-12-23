package com.example.pharmacy.cache;

import com.example.pharmacy.config.RedisConfig;
import com.example.pharmacy.dto.PharmacyDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import({RedisConfig.class})
class PharmacyRedisTemplateServiceTest {

    @MockBean
    private RedisTemplate<String, Object> redisTemplate;
    @MockBean
    private ObjectMapper objectMapper;

    @Autowired
    private PharmacyRedisTemplateService pharmacyRedisTemplateService;

    @BeforeEach
    public void setUp() {
        pharmacyRedisTemplateService.findAll()
                .forEach(dto -> {
                    pharmacyRedisTemplateService.delete(dto.id());
                });
    }

    @Test
    public void redisSaveTest() throws Exception{
        //given
        String pharmacyName = "name";
        String pharmacyAddress = "address";

        PharmacyDto dto =
                PharmacyDto.builder()
                        .id(1L)
                        .pharmacyName(pharmacyName)
                        .pharmacyAddress(pharmacyAddress)
                        .build();

        //when
        pharmacyRedisTemplateService.save(dto);
        List<PharmacyDto> result = pharmacyRedisTemplateService.findAll();

        //then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).id()).isEqualTo(1L);
        assertThat(result.get(0).pharmacyName()).isEqualTo(pharmacyName);
        assertThat(result.get(0).pharmacyAddress()).isEqualTo(pharmacyAddress);
    }

    @Test
    public void redisFailTest() throws Exception{
        //given
        PharmacyDto dto =
                PharmacyDto.builder()
                        .build();

        //when
        pharmacyRedisTemplateService.save(dto);
        List<PharmacyDto> result = pharmacyRedisTemplateService.findAll();

        //then
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void redisDeleteTest() throws Exception{
        //given
        String pharmacyName = "name";
        String pharmacyAddress = "address";

        PharmacyDto dto =
                PharmacyDto.builder()
                        .id(1L)
                        .pharmacyName(pharmacyName)
                        .pharmacyAddress(pharmacyAddress)
                        .build();

        //when
        pharmacyRedisTemplateService.save(dto);
        pharmacyRedisTemplateService.delete(dto.id());
        List<PharmacyDto> result = pharmacyRedisTemplateService.findAll();

        //then
        assertThat(result.size()).isEqualTo(0);
    }
}