package com.example.pharmacy.dto;

import lombok.Builder;

@Builder
public record OutputDto(
        String pharmacyName,    // 약국 명
        String pharmacyAddress, // 약국 주소
        String directionUrl,    // 길안내 url
        String roadViewUrl,     // 로드뷰 url
        String distance         // 고객 주소와 약국 주소의 거리
) {
}
