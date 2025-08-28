package com.skytracker.pricecollector.service;

import com.skytracker.pricecollector.util.RouteStoreUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PriceCollectConsume {

    private final RouteStoreUtil routeStoreUtil;

    @KafkaListener(topics = "flight-ticket-update", groupId = "price-collector-group")
    public void TicketConsume(List<Object> responseDto, Acknowledgment ack) {
        try {
            routeStoreUtil.routeStore(responseDto);
            ack.acknowledge();
        } catch (Exception e) {
            throw new IllegalArgumentException("항공권 결과 저장 실패", e);
        }
    }
}