package com.example.pharmacy.repository;

import com.example.pharmacy.entity.Pharmacy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PharmacyRepositoryTest {

    @Autowired
    private PharmacyRepository pharmacyRepository;

    @Test
    public void pharmacyRepositorySave() throws Exception{
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

}