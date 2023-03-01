package com.example.gpt3javaexample;

import com.example.gpt3javaexample.util.SoundPlayer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Gpt3JavaExampleApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
//        SpringApplication.run(Gpt3JavaExampleApplication.class, args);
        new SoundPlayer().play("привет котик");
    }
}
