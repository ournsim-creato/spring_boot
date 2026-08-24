package com.spring_boot_api_p2.feature.intergration.redis;

import java.time.Duration;
import java.util.Optional;

public interface RedisService {
    // map<Key, Value>
    void save(String key, String value, Duration ttl);
    void save(String key, String value);
    Optional<String> get(String key);
    boolean remove(String keys);
    boolean exists(String key);
    void delete(String key);
}