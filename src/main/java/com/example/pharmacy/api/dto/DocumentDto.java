package com.example.pharmacy.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDto {   // https://developers.kakao.com/docs/latest/ko/local/dev-guide
    // 연습을 위해 class로 생성
    @JsonProperty("address_name")
    private String addressName; // 전체 지번 주소 또는 전체 도로명 주소, 입력에 따라 결정됨

    @JsonProperty("y")
    private double latitude;

    @JsonProperty("x")
    private double longitude;
}
