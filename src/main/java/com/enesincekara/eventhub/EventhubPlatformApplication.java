package com.enesincekara.eventhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EventhubPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventhubPlatformApplication.class, args);
    }

}
