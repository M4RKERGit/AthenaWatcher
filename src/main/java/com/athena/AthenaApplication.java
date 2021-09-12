package com.athena;

import com.athena.linuxtools.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AthenaApplication
{
    private static final Logger logger = new Logger("[BEG]");
    public static void main(String[] args)
    {

        logger.createLog("Starting Athena...");
        Runtime.getRuntime().addShutdownHook
                (new Thread(() -> logger.createLog("Disabling Athena...")));
        SpringApplication.run(AthenaApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup()
    {
        logger.createLog("Loading completed, all services running");
        AthenaSettings.printSettingAfterLoading();
    }
}
