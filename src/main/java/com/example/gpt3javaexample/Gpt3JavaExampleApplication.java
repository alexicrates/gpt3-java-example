package com.example.gpt3javaexample;

import com.example.gpt3javaexample.utils.speaker.SoundPlayer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class Gpt3JavaExampleApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication springApplication = new SpringApplication(Gpt3JavaExampleApplication.class);
        springApplication.run(args);
//        new SoundPlayer().play("Алиса");
    }
}
