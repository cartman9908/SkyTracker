package com.skytracker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skytracker.common.dto.flightSearch.FlightSearchResponseDto;
import com.skytracker.common.exception.integrations.JsonMappingFailedException;
import com.skytracker.core.constants.RedisKeys;
import com.skytracker.core.service.RedisService;
import com.skytracker.dto.FlightTicketDto;
import com.skytracker.dto.HotRouteBestPrice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HotRankingService {

    private final RedisService redisService;
    private final ObjectMapper objectMapper;

    public List<HotRouteBestPrice> getHotRouteBestPrice() {

        List<HotRouteBestPrice> hotRouteBestPrices = new ArrayList<>();

        List<String> sortedKeys = redisService.getKeys(RedisKeys.HOT_ROUTES + ":*");

        for (String key : sortedKeys) {
            try {
                List<Object> values = redisService.getList(key);

                List<FlightTicketDto> tickets = classifyDto(values);

                tickets.stream()
                        .min(Comparator.comparing(FlightTicketDto::getPrice))
                        .map(HotRouteBestPrice::from)
                        .ifPresent(hotRouteBestPrices::add);

            } catch (JsonProcessingException e) {
                throw new JsonMappingFailedException("역직렬화 실패: JSON 구조가 DTO와 맞지 않습니다", e);
            }
        }
        return hotRouteBestPrices;
    }

    /**
     * Redis에 저장된 JSON 리스트를 FlightTicketDto 리스트로 변환
     */
    private List<FlightTicketDto> classifyDto(List<Object> values) throws JsonProcessingException {
        List<FlightTicketDto> flightTicketDtos = new ArrayList<>();

        try {
            for (Object value : values) {
                JsonNode node = objectMapper.readTree(value.toString());

                // tripType은 필요하면 검증용으로만 사용 (ONE_WAY / ROUND_TRIP)
                String tripType = node.path("tripType").asText();
                if (!"ONE_WAY".equals(tripType) && !"ROUND_TRIP".equals(tripType)) {
                    // 이상한 값이면 스킵하거나 로그 남기기 선택
                    continue;
                }

                FlightSearchResponseDto dto =
                        objectMapper.treeToValue(node, FlightSearchResponseDto.class);

                // legs 기반 FlightTicketDto 생성
                flightTicketDtos.add(FlightTicketDto.from(dto));
            }
        } catch (JsonMappingException e) {
            throw new JsonMappingFailedException("역직렬화 실패: JSON 구조가 DTO와 맞지 않습니다", e);
        }

        return flightTicketDtos;
    }
}