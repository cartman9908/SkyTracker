package com.skytracker.pricecollector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.skytracker.core")
@SpringBootApplication
public class PriceCollectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(PriceCollectorApplication.class, args);
    }

}
