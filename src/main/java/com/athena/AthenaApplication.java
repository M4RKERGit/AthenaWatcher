package com.athena;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Collections;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class AthenaApplication
{
    public static void main(String[] args)
    {
        log.info("Starting Athena...");
        Runtime.getRuntime().addShutdownHook
                (new Thread(() -> log.info("Disabling Athena...")));
        SpringApplication app = new SpringApplication(AthenaApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", AthenaSettings.getServerPort()));
        app.run(args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup()
    {
        log.info("Loading completed, all services running");
        AthenaSettings.printSettingAfterLoading();
    }
}
