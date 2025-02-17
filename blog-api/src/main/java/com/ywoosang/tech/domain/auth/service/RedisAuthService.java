package com.ywoosang.tech.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import java.time.Duration;

// 공식문서 StringRedisTemplate
// https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/core/StringRedisTemplate.html
@Component
@RequiredArgsConstructor
public class RedisAuthService {
    private final StringRedisTemplate redisTemplate;

    public void setRefreshToken(Long memberId , String value, int expiresIn) {
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        String key = generateRefreshTokenKey(memberId);
        valueOps.set(key, value, Duration.ofSeconds(expiresIn));
    }

    public String getRefreshToken(Long memberId) {
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        String key = generateRefreshTokenKey(memberId);
        return valueOps.get(key);
    }

    public void deleteValue(Long memberId) {
        String key = generateRefreshTokenKey(memberId);
        redisTemplate.delete(key);
    }

    // refreshToken 을 저장할 key 생성
    public String generateRefreshTokenKey(Long memberId) {
        return "auth:refresh:" + memberId;
    }
}
