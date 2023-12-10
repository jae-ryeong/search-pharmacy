package com.example.pharmacy.repository;

import com.example.pharmacy.entity.Pharmacy;
import com.example.pharmacy.service.PharmacyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PharmacyRepositoryTest {

    @Autowired
    private PharmacyRepository pharmacyRepository;

    @BeforeEach
    public void setUp() {
        pharmacyRepository.deleteAll();
    }

    @Test
    public void pharmacyRepositorySave() throws Exception {
        //given
        String address = "서울 특별시 성북구 종암동";
        String name = "은혜 약국";
        double latitude = 36.11;
        double longitude = 128.11;

        final Pharmacy pharmacy = Pharmacy.builder()
                .pharmacyName(name)
                .pharmacyAddress(address)
                .latitude(latitude)
                .longitude(longitude)
                .build();

        //when
        Pharmacy save = pharmacyRepository.save(pharmacy);

        //then
        assertThat(save.getPharmacyAddress()).isEqualTo(address);
        assertThat(save.getPharmacyName()).isEqualTo(name);
        assertThat(save.getLatitude()).isEqualTo(latitude);
        assertThat(save.getLongitude()).isEqualTo(longitude);
    }

    @Test
    public void pharmacySaveAll() throws Exception {
        //given
        String address = "서울 특별시 성북구 종암동";
        String name = "은혜 약국";
        double latitude = 36.11;
        double longitude = 128.11;

        final Pharmacy pharmacy1 = Pharmacy.builder()
                .pharmacyName(name)
                .pharmacyAddress(address)
                .latitude(latitude)
                .longitude(longitude)
                .build();

        //when
        pharmacyRepository.saveAll(List.of(pharmacy1));
        List<Pharmacy> result = pharmacyRepository.findAll();

        //then
        assertThat(result.size()).isEqualTo(1);
    }

    @DisplayName("BaseTimeEntity(Auditing) 등록")
    @Test
    public void baseTimeTest() throws Exception {
        //given
        LocalDateTime now = LocalDateTime.now();
        String address = "서울 특별시 성북구 종암동";
        String name = "은혜 약국";

        Pharmacy pharmacy = Pharmacy.builder()
                .pharmacyName(name)
                .pharmacyAddress(address)
                .build();

        //when
        Pharmacy result = pharmacyRepository.save(pharmacy);

        //then
        assertThat(result.getCreatedDate()).isAfter(now);
        assertThat(result.getModifiedDate()).isAfter(now);
    }
}
