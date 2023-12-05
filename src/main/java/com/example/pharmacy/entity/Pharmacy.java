package com.example.pharmacy.entity;

import com.example.pharmacy.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "pharmacy")
public class Pharmacy extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pharmacyName;
    private String pharmacyAddress;
    private double latitude;
    private double longitude;

    public void changePharmacyAddress(String address) {
        this.pharmacyAddress = address;
    }
}
