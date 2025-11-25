package com.skytracker.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void pushList(String key, String json) {
        redisTemplate.opsForList().rightPush(key, json,Duration.ofMinutes(10));
    }

    public List<String> getKeys(String key) {
        return new ArrayList<>(Objects.requireNonNull(redisTemplate.keys(key)));
    }

    public List<Object> getList(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    public List<Object> getHashValues(String key) {
        return redisTemplate.opsForHash().values(key);
    }

    public Map<Object, Object> getHash(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public void rename(String fromKey, String toKey) {
        redisTemplate.rename(fromKey, toKey);
    }

    public void hashPut(String key, String field, String value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    public void setValueWithTTL(String key, String value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    public String getValue(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        return value != null ? value.toString() : null;
    }

    public Boolean isBlackListed(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return false;
        }
        return Boolean.parseBoolean(value.toString());
    }

    public void setBlackList(String key, boolean value, long ttl, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, String.valueOf(value), ttl, unit);
    }
}
