package com.example.recipeapp.global.lock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockService {

    private final LockRedisRepository lockRedisRepository;

    public void executeWithLock(String key, Runnable task) {
        boolean isLocked = lockRedisRepository.lock(key);

        if (!isLocked) {
            throw new RuntimeException("Lock 획득 실패");
        }

        try {
            task.run();
        } finally {
            lockRedisRepository.unlock(key);
        }
    }
}