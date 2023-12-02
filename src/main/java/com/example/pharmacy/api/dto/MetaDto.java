package com.example.pharmacy.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MetaDto(  // https://developers.kakao.com/docs/latest/ko/local/dev-guide
        @JsonProperty("total_count") // Json으로 응답을 받을 때 이거를 현재 매핑된 필드와 매핑 시켜주는 어노테이션
        Integer totalCount      // 검색어에 검색된 문서 수
) {
}
