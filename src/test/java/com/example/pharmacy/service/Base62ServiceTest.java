package com.example.pharmacy.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class Base62ServiceTest {

    @InjectMocks
    private Base62Service base62Service;

    @Test
    public void base62EncoderAndDecoderTest() throws Exception{
        //given
        long num = 5;

        //when
        String encodedId = base62Service.encodeDirectionId(num);
        Long decodedId = base62Service.decodeDirectionId(encodedId);

        //then
        System.out.println("encodedId = " + encodedId);
        assertThat(num).isEqualTo(decodedId);
    }

}