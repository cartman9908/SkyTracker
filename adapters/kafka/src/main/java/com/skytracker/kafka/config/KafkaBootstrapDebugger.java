package com.skytracker.kafka.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaBootstrapDebugger {

    private final Environment env;

    @PostConstruct
    public void logKafkaBootstrap() {
        log.info("spring.kafka.bootstrap-servers = {}", env.getProperty("spring.kafka.bootstrap-servers"));
        log.info("SPRING_KAFKA_BOOTSTRAP_SERVERS = {}", System.getenv("SPRING_KAFKA_BOOTSTRAP_SERVERS"));
        log.info("KAFKA_BOOTSTRAP_SERVERS = {}", System.getenv("KAFKA_BOOTSTRAP_SERVERS"));
    }
}