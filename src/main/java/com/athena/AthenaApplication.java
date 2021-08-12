package com.athena;

import com.athena.linuxtools.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AthenaApplication
{
    public static void main(String[] args)
    {
        Logger logger = new Logger("[BEG]");
        logger.createLog("Starting Athena...");
        Runtime.getRuntime().addShutdownHook
                (new Thread(() -> {logger.createLog("Disabling Athena...");}));
        SpringApplication.run(AthenaApplication.class, args);
    }
}
