package com.skytracker.service;

import com.skytracker.common.dto.RouteAggregationDto;
import com.skytracker.common.dto.flightSearch.FlightSearchRequestDto;
import com.skytracker.common.exception.kafka.FlightTicketPublishFailedException;
import com.skytracker.common.exception.integrations.HotRouteParsedFailed;
import com.skytracker.common.exception.integrations.RouteAggregationException;
import com.skytracker.core.constants.RedisKeys;
import com.skytracker.core.mapper.FlightSearchResponseMapper;
import com.skytracker.core.service.AmadeusFlightSearchService;
import com.skytracker.core.service.RedisService;
import com.skytracker.kafka.service.TicketUpdateProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FlightTrackingService {

    private final TicketUpdateProducer producer;
    private final AmadeusFlightSearchService amadeusService;
    private final RedisService redisService;

    /**
     * 가격 수집 및 가격변동 이벤트 발행 (9분)
     */
    @Scheduled(cron = "0 */1 * * * *")
    public void collectAndPublishPrices() {
        try {
            String accessToken = redisService.getValue(RedisKeys.AMADEUS_TOKEN);
            List<RouteAggregationDto> cachedHotRoutes = getCachedHotRoutes();

            int totalPublished = 0;

            for (RouteAggregationDto route : cachedHotRoutes) {
                FlightSearchRequestDto req = FlightSearchResponseMapper.toFlightSearchRequestDto(route);

                List<?> responses = amadeusService.searchFlights(accessToken, req);
                for (Object responseDto : responses) {
                    producer.sendTicketUpdate(responseDto);
                }
            }
            log.info("항공권 가격 수집 및 Kafka 발행 완료 ({}건)", totalPublished);
        } catch (Exception e) {
            throw new FlightTicketPublishFailedException("항공권 가격정보 Kafka 발행 중 오류", e);
        }
    }

    /**
     * Redis 에서 상위 인기 노선 10개 Get
     */
    private List<RouteAggregationDto> getCachedHotRoutes() {
        List<Object> rawList = redisService.getHashValues(RedisKeys.HOT_ROUTES);
        List<RouteAggregationDto> result = new ArrayList<>(rawList.size());
        for (Object json : rawList) {
            RouteAggregationDto dto = parseRouteAggregationDto(String.valueOf(json));
            result.add(dto);
        }
        return result;
    }

    private RouteAggregationDto parseRouteAggregationDto(String s) {
        String[] t = s.split("_");
        try {
            if (t.length == 6) {
                return new RouteAggregationDto(
                        t[0], t[1], t[2], t[3],
                        Integer.parseInt(t[4]),
                        Long.parseLong(t[5])
                );
            } else if (t.length == 5) {
                return new RouteAggregationDto(
                        t[0], t[1], t[2],
                        null, Integer.parseInt(t[3]),
                        Long.parseLong(t[4])
                );
            } else {
                throw new HotRouteParsedFailed(s);
            }
        } catch (NumberFormatException e) {
            throw new RouteAggregationException("유효하지 않은 HOT_ROUTES 포맷: " + s);        }
    }
}