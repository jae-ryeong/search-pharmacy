package com.example.pharmacy.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record KakaoApiResponseDto(
        @JsonProperty("meta")
        MetaDto metaDto,        // 몇 건이 검색 되었는지
        @JsonProperty("documents")
        List<DocumentDto> documentList
) {
}
