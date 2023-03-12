package com.example.gpt3javaexample;

import com.example.gpt3javaexample.services.ListenerService;
import com.example.gpt3javaexample.web.client.ListenerClient;
import com.example.gpt3javaexample.utils.speaker.SoundPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.io.IOException;

@SpringBootApplication
@EnableFeignClients
public class Gpt3JavaExampleApplication {

    public static String ONLY_SPEECH = "only_speech.wav";

    public static ListenerClient client = null;

    @Autowired
    public Gpt3JavaExampleApplication(ListenerClient client) {
        this.client = client;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
//        SpringApplication.run(Gpt3JavaExampleApplication.class, args);
//        new SoundPlayer().play("Добрый день");

        new ListenerService().startListening();

//        Process exec = Runtime.getRuntime().exec("whisper test.wav --language Russian");
//        System.out.println(new String(exec.getInputStream().readAllBytes()));

    }
}
