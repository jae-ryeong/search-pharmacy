package com.example.pharmacy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
public record PharmacyDto(
        Long id,
        String pharmacyName,
        String pharmacyAddress,
        double latitude,
        double longitude) {
}
