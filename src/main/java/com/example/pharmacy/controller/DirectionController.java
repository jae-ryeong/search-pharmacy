package com.example.pharmacy.controller;

import com.example.pharmacy.entity.Direction;
import com.example.pharmacy.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DirectionController {

    private final DirectionService directionService;
    private static final String DIRECTION_BASE_URL = "https://map.kakao.com/link/map/";

    @GetMapping("/dir/{encodedId}")
    public String searchDirection(@PathVariable("encodedId") String encodedId) {    // shortenURL
        // encodedId는 인코딩 되어있으니 디코딩 해줘야한다.

        Direction resultDirection = directionService.findById(encodedId);

        String params = String.join(",", resultDirection.getTargetPharmacyName(),
                String.valueOf(resultDirection.getTargetLatitude()), String.valueOf(resultDirection.getTargetLongitude()));
        // ex: 은혜약국,38.11,128.11

        String result = UriComponentsBuilder.fromHttpUrl(DIRECTION_BASE_URL + params)
                .toUriString();

        log.info("direction params: {}, url: {}", params, result);

        return "redirect:" + result;
    }
}
