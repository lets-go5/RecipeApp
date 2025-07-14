package com.example.recipeapp.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        // 타입 정보 안붙임
        return objectMapper;
    }

    // 캐시 저장/복원용 (타입 정보 포함)
    @Bean(name = "cacheObjectMapper")
    public ObjectMapper cacheObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.activateDefaultTyping(  // 데이터 타입 깉이 가져오기
                BasicPolymorphicTypeValidator.builder().allowIfSubType(Object.class).build(), // 모든데이터 타입 허용
                ObjectMapper.DefaultTyping.EVERYTHING // 객체에 타입 정보 붙이기
        );
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
