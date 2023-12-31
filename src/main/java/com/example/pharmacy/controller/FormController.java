package com.example.pharmacy.controller;

import com.example.pharmacy.dto.InputDto;
import com.example.pharmacy.dto.OutputDto;
import com.example.pharmacy.service.PharmacyRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class FormController {

    private final PharmacyRecommendationService pharmacyRecommendationService;

    @GetMapping("/")
    public String main() {
        return "main";
    }

    @PostMapping("/search")
    public ModelAndView postDirection(@ModelAttribute InputDto inputDto) {
        List<OutputDto> outputDtos = pharmacyRecommendationService.recommendPharmacyList(inputDto.address());
        System.out.println("outputDtos = " + outputDtos);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("output");
        modelAndView.addObject("outputFormList", outputDtos);

        return modelAndView;
    }
}
