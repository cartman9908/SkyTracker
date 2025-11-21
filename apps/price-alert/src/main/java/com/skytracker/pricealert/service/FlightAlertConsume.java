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
        log.info("Received event {}", eventMessageDto);
        try {
            sendMailService.sendMail(eventMessageDto);
            log.info("Mail sent successfully for alert: {}", eventMessageDto);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Failed to send mail for alert: {}", eventMessageDto, e);
            throw e;
        }
    }
}