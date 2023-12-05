package com.example.pharmacy.service;

import com.example.pharmacy.entity.Pharmacy;
import com.example.pharmacy.repository.PharmacyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PharmacyServiceTest {

    @Autowired
    private PharmacyService pharmacyService;
    @Autowired
    private PharmacyRepository pharmacyRepository;

    @BeforeEach
    public void setUp() {
        pharmacyRepository.deleteAll();
    }

    @DisplayName("더티 체킹 OK")
    @Test
    public void dirtyCheckingTest() throws Exception{
        //given
        String address = "서울 특별시 성북구 종암동";
        String modifiedAddress = "대전시";
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
        Pharmacy entity = pharmacyRepository.save(pharmacy);
        pharmacyService.updateAddress(entity.getId(), modifiedAddress);

        List<Pharmacy> result = pharmacyRepository.findAll();

        //then
        assertThat(result.get(0).getPharmacyAddress()).isEqualTo("대전시");
    }

    @DisplayName("더티 체킹 No")
    @Test
    public void noDirtyCheckingTest() throws Exception{
        //given
        String address = "서울 특별시 성북구 종암동";
        String modifiedAddress = "대전시";
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
        Pharmacy entity = pharmacyRepository.save(pharmacy);
        pharmacyService.updateAddressNoTransaction(entity.getId(), modifiedAddress);

        List<Pharmacy> result = pharmacyRepository.findAll();

        //then
        assertThat(result.get(0).getPharmacyAddress()).isEqualTo(address);
    }
}