package com.skytracker.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skytracker.common.dto.flightSearch.FlightSearchResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FlightSearchCache {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public boolean hasKey(String key) {
        // null-safe boolean 처리
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public List<FlightSearchResponseDto> cacheSearch(String key) {
        Object cached = redisTemplate.opsForValue().get(key);
        if (cached == null) {
            log.info("No cached flight search response");
            return null;
        }

        try {
            String json = cached.toString();
            return objectMapper.readValue(json, new TypeReference<List<FlightSearchResponseDto>>() {});
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize cache value for key={}", key, e);
            return null; // 역직렬화 실패 시 캐시 무시하고 새로 조회하게
        }
    }

    public void putSearch(String uniqueKey,List<FlightSearchResponseDto> dto){
        try {
            String json = objectMapper.writeValueAsString(dto);
            redisTemplate.opsForValue().set(uniqueKey, json, Duration.ofMinutes(5));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
