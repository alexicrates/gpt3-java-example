package com.example.gptapi;

import com.example.gptapi.services.speaker.SoundPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GptApiApplication implements CommandLineRunner {

    SoundPlayer soundPlayer = new SoundPlayer();

    public static void main(String[] args) {
        SpringApplication.run(GptApiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
