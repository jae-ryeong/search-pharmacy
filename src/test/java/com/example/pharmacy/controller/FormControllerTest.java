package com.example.pharmacy.controller;

import com.example.pharmacy.dto.OutputDto;
import com.example.pharmacy.service.PharmacyRecommendationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FormController.class)
@MockBean(JpaMetamodelMappingContext.class) // JPA metamodel must not be empty 에러 해결
class FormControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PharmacyRecommendationService pharmacyRecommendationService;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void mainGetTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(handler().handlerType(FormController.class))
                .andExpect(handler().methodName("main"))
                .andExpect(status().isOk())
                .andExpect(view().name("main"))
                .andDo(log());
    }

    @Test
    public void searchPostTest() throws Exception{
        //given
        String inputAddress = "서울 성북구 종암동";

        ArrayList<OutputDto> outputDtoList = new ArrayList<>();

        outputDtoList.add(
                OutputDto.builder()
                        .pharmacyName("pharmacy1")
                        .build());

        outputDtoList.add(
                OutputDto.builder()
                        .pharmacyName("pharmacy2")
                        .build()
        );

        //when
        when(pharmacyRecommendationService.recommendPharmacyList(inputAddress)).thenReturn(outputDtoList);

        ResultActions resultActions = mockMvc.perform(post("/search")
                .param("address", inputAddress));

        //then
        System.out.println("outputDtoList = " + outputDtoList);

        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name("output"))
                .andExpect(model().attributeExists("outputFormList")) // model에 outputFormList라는 key가 존재하는지 확인
                .andExpect(model().attribute("outputFormList", outputDtoList))
                .andDo(print());
    }


}