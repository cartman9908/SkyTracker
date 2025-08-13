<<<<<<<< HEAD:adapters/kafka/src/main/java/com/skytracker/kafka/service/TicketUpdateProducer.java
package com.skytracker.kafka.service;
========
package com.skytracker.kafkaproducer.service;
>>>>>>>> d45b0dd ("Kafka Producer 설정, ES 설정 집계 수정"):apps/kafka-producer/src/main/java/com/skytracker/kafkaproducer/service/FlightPriceUpdateProducer.java

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketUpdateProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

        public void sendTicketUpdate(Object responseDto) {
        kafkaTemplate.send("flight-ticket-update", responseDto);
    }
}