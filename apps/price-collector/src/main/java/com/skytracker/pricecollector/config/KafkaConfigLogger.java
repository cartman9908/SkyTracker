package com.skytracker.pricecollector.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConfigLogger implements CommandLineRunner {

    private final Environment environment;

    @Override
    public void run(String... args) {
        String bootstrap = environment.getProperty("spring.kafka.bootstrap-servers");
        String groupId   = environment.getProperty("spring.kafka.consumer.group-id");
        String autoReset = environment.getProperty("spring.kafka.consumer.auto-offset-reset");

        log.info("### Kafka config check ###");
        log.info("spring.kafka.bootstrap-servers = {}", bootstrap);
        log.info("spring.kafka.consumer.group-id = {}", groupId);
        log.info("spring.kafka.consumer.auto-offset-reset = {}", autoReset);
    }
}