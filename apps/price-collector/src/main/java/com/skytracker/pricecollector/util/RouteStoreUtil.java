package com.skytracker.pricecollector.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skytracker.common.dto.flightSearch.FlightSearchResponseDto;
import com.skytracker.common.exception.integrations.RouteKeyNotFoundException;
import com.skytracker.core.constants.RedisKeys;
import com.skytracker.core.service.RedisClient;
import com.skytracker.pricecollector.dto.SortedRouteDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class RouteStoreUtil {

    private final RedisClient redisClient;
    private final ObjectMapper objectMapper;

    public void routeStore(List<FlightSearchResponseDto> dtoList) throws JsonProcessingException {

        Map<Object, Object> rankingMap = redisClient.getHash(RedisKeys.HOT_ROUTES);

        for (FlightSearchResponseDto responseDto : dtoList) {

            String json = objectMapper.writeValueAsString(responseDto);

            SortedRouteDto sortedRouteDto = SortedRouteDto.from(responseDto);

            pushToRouteList(rankingMap, json, sortedRouteDto);
        }
    }

    private void pushToRouteList(Map<Object,Object> rankingMap, String json, SortedRouteDto sortedRouteDto) {

        String routeKey = getRouteKey(sortedRouteDto);

        String matchedRankField = rankingMap.entrySet().stream()
                .map(e -> Map.entry(String.valueOf(e.getKey()), String.valueOf(e.getValue())))
                .filter(e -> e.getValue().startsWith(routeKey + "_"))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        if (matchedRankField != null) {
            redisClient.pushList(routeKey, json);
            log.info("push this key {}", matchedRankField);
        } else {
            throw new RouteKeyNotFoundException("해당 경로에 대한 랭킹 키를 찾을 수 없습니다. routeKey=" + routeKey);
        }
    }

    private String getRouteKey(SortedRouteDto dto) {

        String departureAirport = dto.getDepartureAirportCode();
        String arrivalAirport = dto.getArrivalAirportCode();
        String departureTime = dto.getDepartureTime();
        String arrivalTime = dto.getArrivalTime();

        return (dto.getArrivalTime() == null)
                ? String.join("_", departureAirport, arrivalAirport, departureTime)
                : String.join("_", departureAirport, arrivalAirport, departureTime, arrivalTime);
    }
}