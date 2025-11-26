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

@Service
@Slf4j
@RequiredArgsConstructor
public class RouteStoreUtil {

    private final RedisClient redisClient;
    private final ObjectMapper objectMapper;

    public void routeStore(List<FlightSearchResponseDto> dtoList) throws JsonProcessingException {

        List<String> rankingList = redisClient.getList(RedisKeys.HOT_ROUTES);

        for (FlightSearchResponseDto responseDto : dtoList) {

            String json = objectMapper.writeValueAsString(responseDto);

            SortedRouteDto sortedRouteDto = SortedRouteDto.from(responseDto);

            pushToRouteList(rankingList, json, sortedRouteDto);
        }
    }

    private void pushToRouteList(List<String> rankingList, String json, SortedRouteDto sortedRouteDto) {

        String routeKey = getRouteKey(sortedRouteDto);

        boolean exists = rankingList.stream()
                .anyMatch(k -> k.equals(routeKey));

        if (exists) {
            redisClient.setValueWithTTL(routeKey, json, Duration.ofMinutes(9));
            log.info("push this key {}", routeKey);
        } else {
            throw new RouteKeyNotFoundException("해당 경로에 대한 랭킹 키를 찾을 수 없습니다. routeKey=" + routeKey);
        }
    }

    private String getRouteKey(SortedRouteDto dto) {

        String dep = dto.getDepartureAirportCode();
        String arr = dto.getArrivalAirportCode();
        String depDate = dto.getDepartureTime();
        String retDate = dto.getArrivalTime();

        if (retDate == null) {
            return String.join(":", dep, arr, depDate);
        }
        return String.join(":", dep, arr, depDate, retDate);
    }
}