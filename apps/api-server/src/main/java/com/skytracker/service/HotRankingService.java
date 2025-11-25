package com.skytracker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skytracker.common.dto.flightSearch.FlightSearchResponseDto;
import com.skytracker.common.exception.integrations.JsonMappingFailedException;
import com.skytracker.core.constants.RedisKeys;
import com.skytracker.core.service.RedisClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HotRankingService {

    private final RedisClient redisClient;
    private final ObjectMapper objectMapper;

    /**
     * 각 HOT_ROUTES:n 리스트에서 "최저가 1건"씩 뽑아서 반환
     * 결과: 인기 노선별 최저가 티켓 목록
     */
    public List<FlightSearchResponseDto> getHotRouteBestPrice() {

        List<FlightSearchResponseDto> result = new ArrayList<>();

        List<String> keys = redisClient.getKeys(RedisKeys.HOT_ROUTES + ":*");

        keys.sort(Comparator.naturalOrder());

        for (String key : keys) {
            try {
                // Redis 리스트에 쌓인 JSON 문자열들
                List<Object> values = redisClient.getList(key);

                // JSON → DTO 리스트
                List<FlightSearchResponseDto> tickets = classifyDto(values);

                // 해당 노선(HOT_ROUTES:n)에서 최저가 티켓 1건만 선택
                tickets.stream()
                        .min(Comparator.comparing(FlightSearchResponseDto::getTotalPrice))
                        .ifPresent(result::add);

            } catch (JsonProcessingException e) {
                throw new JsonMappingFailedException("역직렬화 실패: JSON 구조가 DTO와 맞지 않습니다", e);
            }
        }

        return result;
    }

    /**
     * Redis에 저장된 JSON 리스트를 FlightSearchResponseDto 리스트로 변환
     */
    private List<FlightSearchResponseDto> classifyDto(List<Object> values) throws JsonProcessingException {
        List<FlightSearchResponseDto> result = new ArrayList<>();

        try {
            for (Object value : values) {
                if (value == null) {
                    continue;
                }

                JsonNode node = objectMapper.readTree(value.toString());

                // tripType 필터 (원하면 유지, 아니면 삭제해도 됨)
                String tripType = node.path("tripType").asText();
                if (!"ONE_WAY".equals(tripType) && !"ROUND_TRIP".equals(tripType)) {
                    // 이상한 값이면 스킵
                    continue;
                }

                FlightSearchResponseDto dto =
                        objectMapper.treeToValue(node, FlightSearchResponseDto.class);

                result.add(dto);
            }
        } catch (JsonMappingException e) {
            throw new JsonMappingFailedException("역직렬화 실패: JSON 구조가 DTO와 맞지 않습니다", e);
        }

        return result;
    }
}