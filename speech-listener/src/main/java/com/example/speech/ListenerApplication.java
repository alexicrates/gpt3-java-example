package com.example.speech;

import com.example.speech.audio.detectors.TriggerWordDetector;
import com.example.speech.web.clients.SileroTTSClient;
import com.example.speech.web.clients.WhisperSTTClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients
public class ListenerApplication implements CommandLineRunner {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private WhisperSTTClient sttClient;

    @Autowired
    private TriggerWordDetector triggerWordDetector;

    @Autowired
    private SileroTTSClient sileroTTSClient;

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(ListenerApplication.class);
        builder.headless(false);
        ConfigurableApplicationContext context = builder.run(args);
    }

    @Override
    public void run(String... args) {
    }
}
