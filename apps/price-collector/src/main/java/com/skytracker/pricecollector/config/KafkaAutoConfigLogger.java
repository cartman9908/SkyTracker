package com.skytracker.pricecollector.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaAutoConfigLogger implements CommandLineRunner {

    private final KafkaProperties kafkaProperties;

    @Override
    public void run(String... args) {
        log.info("### KafkaProperties check ###");
        log.info("bootstrapServers = {}", kafkaProperties.getBootstrapServers());
        log.info("clientId         = {}", kafkaProperties.getClientId());
        log.info("consumer.groupId = {}", kafkaProperties.getConsumer().getGroupId());
    }
}