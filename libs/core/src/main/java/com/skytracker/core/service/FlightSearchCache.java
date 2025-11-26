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
import java.util.ArrayList;
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
        try {
            List<Object> cachedList = redisTemplate.opsForList().range(key, 0, -1);
            if (cachedList == null || cachedList.isEmpty()) {
                log.info("No cached list found for key={}", key);
                return null;
            }

            List<FlightSearchResponseDto> results = new ArrayList<>();
            for (Object obj : cachedList) {
                String json = obj.toString();
                FlightSearchResponseDto dto = objectMapper.readValue(json, FlightSearchResponseDto.class);
                results.add(dto);
            }

            log.info("Cache HIT[List]: {} (size={})", key, results.size());
            return results;

        } catch (Exception e) {
            log.error("Failed to deserialize Redis LIST key={}", key, e);
            return null;
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
