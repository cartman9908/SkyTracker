package com.skytracker.elasticsearch.service;

import com.skytracker.core.constants.RedisKeys;
import com.skytracker.core.service.RedisClient;
import com.skytracker.dto.EsAggregationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteAggregationService {

    private final ElasticAggregationService esAggregationService;
    private final RedisClient redisClient;

    @Scheduled(cron = "0 0 0 * * *")
    public void updateHotRoutes() {
        try {
            // 1) ES에서 Top 10 가져오기
            List<EsAggregationDto> topRoutes = esAggregationService.getTopRoutes(10);
            topRoutes.sort(Comparator.comparingLong(EsAggregationDto::getDocCount).reversed());

            // 2) uniqueKey 리스트로 변환
            List<String> topUniqueKeys = topRoutes.stream()
                    .map(this::toUniqueKeyFromEsAgg)   // 예: "YVR:ICN:2025-11-02:2025-11-29:1"
                    .toList();

            // 3) 임시 리스트에 먼저 채우기
            String tmpKey = RedisKeys.HOT_ROUTES + ":TMP"; // 예: "HOT_ROUTES:TMP"
            redisClient.delete(tmpKey);

            for (String uniqueKey : topUniqueKeys) {
                // HOT_ROUTES는 랭킹 목록이니까 TTL 없이 그냥 저장해도 됨
                redisClient.pushList(tmpKey, uniqueKey);
            }

            // 4) 원자적으로 교체 (기존 HOT_ROUTES ← TMP로 스위칭)
            redisClient.rename(tmpKey, RedisKeys.HOT_ROUTES); // 예: "HOT_ROUTES"

            log.info("인기 노선(HOT_ROUTES) 리스트 캐싱 성공. size={}", topUniqueKeys.size());
        } catch (Exception e) {
            log.error("인기 노선(HOT_ROUTES) 리스트 캐싱 실패", e);
        }
    }

    private String toUniqueKeyFromEsAgg(EsAggregationDto dto) {
        if (dto.getArrivalDate() == null) {
            return dto.getDepartureAirportCode() + ":" + dto.getArrivalAirport()
                    + ":" + dto.getDepartureDate() + ":" + dto.getAdults();
        }
        return dto.getDepartureAirportCode() + ":" + dto.getArrivalAirport()
                + ":" + dto.getDepartureDate() + ":" + dto.getArrivalDate() + ":" + dto.getAdults();
    }
}