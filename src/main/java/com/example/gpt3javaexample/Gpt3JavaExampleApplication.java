package com.example.gpt3javaexample;

import com.example.gpt3javaexample.util.SoundPlayer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.IOException;

@SpringBootApplication
@EnableAsync
public class Gpt3JavaExampleApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
//        SpringApplication.run(Gpt3JavaExampleApplication.class, args);
//        Process exec = Runtime.getRuntime().exec("whisper test.wav --language Russian");
//        System.out.println(new String(exec.getInputStream().readAllBytes()));
    }
}
