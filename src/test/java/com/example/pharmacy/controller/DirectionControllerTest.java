package com.example.pharmacy.controller;

import com.example.pharmacy.service.DirectionService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DirectionController.class)
@MockBean(JpaMetamodelMappingContext.class)
class DirectionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DirectionService directionService;

    @Test
    public void searchDirectionGetTest() throws Exception{
        //given
        String encodedId = "r";
        String redirectURL = "https://map.kakao.com/link/map/pharmacy,38.11,128.11";

        //when
        when(directionService.findDirectionUrlById(encodedId)).thenReturn(redirectURL);
        ResultActions result = mockMvc.perform(get("/dir/{encodedId}", encodedId));

        //then
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(redirectURL))
                .andDo(print());


    }
}