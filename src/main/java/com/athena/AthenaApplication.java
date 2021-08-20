package com.athena;

import com.athena.linuxtools.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@EnableScheduling
@Configuration
public class AthenaApplication
{
    @Bean
    MultipartConfigElement multipartConfigElement()
    {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofKilobytes(1024));
        factory.setMaxRequestSize(DataSize.ofKilobytes(1024));
        return factory.createMultipartConfig();
    }

    public static void main(String[] args)
    {
        Logger logger = new Logger("[BEG]");
        logger.createLog("Starting Athena...");
        Runtime.getRuntime().addShutdownHook
                (new Thread(() -> {logger.createLog("Disabling Athena...");}));
        SpringApplication.run(AthenaApplication.class, args);
    }
}
