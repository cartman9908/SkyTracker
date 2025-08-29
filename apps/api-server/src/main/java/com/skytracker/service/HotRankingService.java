package com.skytracker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skytracker.common.dto.flightSearch.FlightSearchResponseDto;
import com.skytracker.common.dto.flightSearch.RoundTripFlightSearchResponseDto;
import com.skytracker.common.exception.JsonMappingFailedException;
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

        List<String> SortedKeys = redisService.getKeys(RedisKeys.HOT_ROUTES + ":*");

        for (String key : SortedKeys) {
            try {
                List<Object> values = redisService.getList(key);

                List<FlightTicketDto> tickets = classifyDto(values);

                tickets.stream()
                        .min(Comparator.comparing(FlightTicketDto::getPrice))
                        .ifPresent(flightTicketDto -> {
                            HotRouteBestPrice.from(flightTicketDto);
                            hotRouteBestPrices.add(HotRouteBestPrice.from(flightTicketDto));
                        });

            } catch (JsonProcessingException e) {
                throw new JsonMappingFailedException("역직렬화 실패: JSON 구조가 DTO와 맞지 않습니다", e);
            }

        }
        return hotRouteBestPrices;
    }

    private List<FlightTicketDto> classifyDto(List<Object> values) throws JsonProcessingException {
        List<FlightTicketDto> flightTicketDto = new ArrayList<>();
        try {
            for (Object value : values) {
                JsonNode node = objectMapper.readTree(value.toString());

                String tripType = node.get("tripType").asText();

                if ("ROUND_TRIP".equals(tripType)) {
                    RoundTripFlightSearchResponseDto dto =
                            objectMapper.treeToValue(node, RoundTripFlightSearchResponseDto.class);
                    flightTicketDto.add(FlightTicketDto.from(dto));
                } else if ("ONE_WAY".equals(tripType)) {
                    FlightSearchResponseDto dto =
                            objectMapper.treeToValue(node, FlightSearchResponseDto.class);
                    flightTicketDto.add(FlightTicketDto.from(dto));
                }

            }
        } catch (JsonMappingException e) {
            throw new JsonMappingFailedException("역직렬화 실패: JSON 구조가 DTO와 맞지 않습니다", e);
        }
        return flightTicketDto;
    }
}