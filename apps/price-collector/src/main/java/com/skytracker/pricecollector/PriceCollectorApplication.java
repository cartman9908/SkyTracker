package com.skytracker.pricecollector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {
        "com.skytracker.core",
        "com.skytracker.pricecollector"
})
public class PriceCollectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(PriceCollectorApplication.class, args);
    }

}
