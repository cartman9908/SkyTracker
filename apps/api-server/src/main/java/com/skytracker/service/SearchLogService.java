package com.skytracker.service;

import com.skytracker.common.dto.SearchLogDto;
import com.skytracker.common.dto.flightSearch.FlightSearchRequestDto;
import com.skytracker.kafka.service.FlightSearchLogsProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchLogService {

    private final FlightSearchLogsProducer searchLogsProducer;

    public void publishSearchLog(FlightSearchRequestDto req) {
        try {
            SearchLogDto searchLogDto = SearchLogDto.from(req);
            searchLogsProducer.sendSearchLogs(searchLogDto);
            log.info("search logs publish succeed: {} → {}", req.getOriginLocationAirport(), req.getDestinationLocationAirport());
        } catch (Exception e) {
            throw new IllegalStateException("search logs publish failed: " + e.getMessage());
        }
    }
}