package com.example.recipeapp.global.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisManagerConfig {

    @Bean(name = "redisCacheManager")
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        // 키 직렬화 설정: 문자열 직렬화 사용
        RedisSerializationContext.SerializationPair<String> keySerializer = RedisSerializationContext
                .SerializationPair
                .fromSerializer(new StringRedisSerializer());

        // 값 직렬화 설정: JSON 직렬화 사용
        RedisSerializationContext.SerializationPair<Object> valueSerializer = RedisSerializationContext
                .SerializationPair
                .fromSerializer(new GenericJackson2JsonRedisSerializer());

        // RedisCacheConfiguration에 직렬화 방식 설정
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(keySerializer)  // 키 직렬화 설정
                .serializeValuesWith(valueSerializer); // 값 직렬화 설정


        //캐시 이름 별 TTL 설정
        Map<String, RedisCacheConfiguration> cacheTtl = new HashMap<>();
        cacheTtl.put("dashboardCache", cacheConfiguration.entryTtl(Duration.ofMinutes(1)));
        cacheTtl.put("categoryCache", cacheConfiguration.entryTtl(Duration.ofMinutes(5)));
        cacheTtl.put("summaryCache",cacheConfiguration.entryTtl(Duration.ofMinutes(1)));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(cacheConfiguration)  // 캐시 설정 적용
                .build();
    }
}
