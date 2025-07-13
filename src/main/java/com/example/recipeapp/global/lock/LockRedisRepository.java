package com.example.recipeapp.global.lock;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class LockRedisRepository {

    private final StringRedisTemplate redisTemplate;

    public Boolean lock(String key) {
        return redisTemplate.opsForValue()
                .setIfAbsent(key, "lock", 3, TimeUnit.SECONDS); // TTL 3초
    }

    public Boolean unlock(String key) {
        return redisTemplate.delete(key);
    }
}
