package com.skytracker.controller;

import com.skytracker.common.dto.HotRouteSummaryDto;
import com.skytracker.common.dto.flightSearch.FlightSearchRequestDto;
import com.skytracker.common.dto.flightSearch.FlightSearchResponseDto;
import com.skytracker.core.service.AmadeusFlightSearchService;
import com.skytracker.core.service.FlightSearchCache;
import com.skytracker.service.token.AmadeusTokenManger;
import com.skytracker.service.HotRankingService;
import com.skytracker.service.SearchLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/api/flights")
@RestController
@RequiredArgsConstructor
public class FlightsController {

    private final AmadeusFlightSearchService flightSearchService;
    private final AmadeusTokenManger amadeusService;
    private final HotRankingService rankingService;
    private final SearchLogService searchLogService;
    private final FlightSearchCache flightSearchCache;

    @PostMapping("/search")
    public ResponseEntity<?> searchFlights(@RequestBody @Valid FlightSearchRequestDto dto) {
        try {
            searchLogService.publishSearchLog(dto);
            log.info("Successfully published search log");

            String token = amadeusService.getAmadeusAccessToken();
            String uniqueKey = dto.buildUniqueKey();

            log.info("unique key: {}", uniqueKey);

            List<FlightSearchResponseDto> results;

            // 1. 캐시 조회
            if (flightSearchCache.hasKey(uniqueKey)) {
                results = flightSearchCache.cacheSearch(uniqueKey);
                // 역직렬화 실패 등으로 null 나올 수도 있으니 한 번 더 방어
                if (results != null) {
                    log.info("Cache HIT: {}", uniqueKey);
                    return ResponseEntity.ok(results);
                }
                log.info("Cache key exists but value is invalid, falling back to API: {}", uniqueKey);
            }

            // 2. 캐시 미스 or 캐시 값 문제 시 → API 호출
            results = flightSearchService.searchFlights(token, dto);

            return ResponseEntity.ok(results);
        } catch (Exception e) {
            log.error("Flight search failed", e);
            return ResponseEntity.internalServerError().body("Internal error: " + e.getMessage());
        }
    }

    @GetMapping("/hot-routes")
    public ResponseEntity<List<HotRouteSummaryDto>> getHotRouteBestPrice() {
        List<HotRouteSummaryDto> result = rankingService.getHotRouteSummary();
        return ResponseEntity.ok(result);
    }

}