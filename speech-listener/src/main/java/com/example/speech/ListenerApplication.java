package com.example.speech;

import com.example.speech.audio.detectors.SphinxTriggerWordDetector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients
public class ListenerApplication implements CommandLineRunner {

    @Autowired
    SphinxTriggerWordDetector sphinxTriggerWordDetector;

    public static void main(String[] args) throws IOException {
        // Load the custom logging configuration
        InputStream inputStream = ListenerApplication.class.getResourceAsStream("/logging.properties");
        LogManager.getLogManager().readConfiguration(inputStream);

        SpringApplicationBuilder builder = new SpringApplicationBuilder(ListenerApplication.class);
        builder.headless(false);
        ConfigurableApplicationContext context = builder.run(args);
    }

    @Override
    public void run(String... args) {
//        sphinxTriggerWordDetector.waitForTriggerWord();
    }
}
