package com.skytracker.pricealert.service;

import com.skytracker.common.dto.alerts.FlightAlertEventMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightAlertConsume {

    private final SendMailService sendMailService;

    @KafkaListener(topics = "flight-alert", groupId = "flight-alert-group")
    public void OnMessage(FlightAlertEventMessageDto eventMessageDto, Acknowledgment ack) {
        sendMailService.sendMail(eventMessageDto);

        ack.acknowledge();
    }
}