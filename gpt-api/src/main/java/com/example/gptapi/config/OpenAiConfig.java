package com.example.gptapi.config;

import com.theokanning.openai.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class OpenAiConfig {

    @Value("${openai.token}")
    private String OPENAI_TOKEN;

    @Value("${openai.duration.minutes}")
    private int durationInMinutes;

    @Value("${openai.duration.seconds}")
    private int durationInSeconds;

    @Bean
    public OpenAiService openAiService(){
        OpenAiService openAiService;

        if (durationInMinutes > 0){
            openAiService = new OpenAiService(OPENAI_TOKEN, Duration.ofMinutes(durationInMinutes));
        }
        else if (durationInSeconds > 0) {
            openAiService = new OpenAiService(OPENAI_TOKEN, Duration.ofSeconds(durationInSeconds));
        }
        else {
            openAiService = new OpenAiService(OPENAI_TOKEN, Duration.ofSeconds(30));
        }

        return openAiService;
    }

}
