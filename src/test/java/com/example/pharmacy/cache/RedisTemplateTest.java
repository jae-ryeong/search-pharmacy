package com.example.pharmacy.cache;

import com.example.pharmacy.config.RedisConfig;
import com.example.pharmacy.config.RestTemplateConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import({RedisConfig.class})
public class RedisTemplateTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void redisTemplateTest() throws Exception{
        //given
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String key = "stringKey";
        String value = "hello";

        //when
        valueOperations.set(key, value);


        //then
        Object result = valueOperations.get(key);
        assertThat(result).isEqualTo(value);
    }

    @Test
    public void redisTemplateSetTest() throws Exception{
        //given
        SetOperations setOperations = redisTemplate.opsForSet();
        String key = "setKey";

        //when
        setOperations.add(key, "h", "e", "l", "l", "o");

        //then
        assertThat(setOperations.size(key)).isEqualTo(4);
    }

    @Test
    public void redisTemplateHashTest() throws Exception{
        //given
        HashOperations hashOperations = redisTemplate.opsForHash();
        String key = "hashKey";

        //when
        hashOperations.put(key, "subKey", "value");

        //then
        assertThat(hashOperations.get(key, "subKey")).isEqualTo("value");

        Map entries = hashOperations.entries(key);
        assertThat(entries.keySet().contains("subKey")).isTrue();
        assertThat(entries.values().contains("value")).isTrue();

        assertThat(hashOperations.size(key)).isEqualTo(entries.size());
    }
}
