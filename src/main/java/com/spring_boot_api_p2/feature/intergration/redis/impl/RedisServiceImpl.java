package com.spring_boot_api_p2.feature.intergration.redis.impl;

import com.spring_boot_api_p2.feature.intergration.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private static final Duration DEFAULT_TTL = Duration.ofHours(8);

    // read / write data ទៅ redis
    private final StringRedisTemplate redisTemplate;

    @Override
    public void save(String key, String value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    @Override
    public void save(String key, String value) {
        save(key, value, DEFAULT_TTL);
    }

    @Override
    public Optional<String> get(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }

    @Override
    public boolean remove(String keys) {
        Long deleted = redisTemplate.delete(Arrays.asList(keys));
        return Optional.ofNullable(deleted).orElse(0L) > 0;
    }

    @Override
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public void delete(String key) {

    }
}
