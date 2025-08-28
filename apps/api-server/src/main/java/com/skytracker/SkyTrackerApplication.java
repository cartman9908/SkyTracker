package com.skytracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;

import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableCaching
@EnableScheduling
@EntityScan(basePackages = "com.skytracker.entity")
@EnableJpaRepositories(basePackages = "com.skytracker.repository")
@EnableElasticsearchRepositories(basePackages = "com.skytracker.elasticsearch.repository")
@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = {"com.skytracker"})
public class SkyTrackerApplication {
	public static void main(String[] args) {
		SpringApplication.run(SkyTrackerApplication.class, args);
	}
}