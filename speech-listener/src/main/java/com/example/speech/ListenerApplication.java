package com.example.speech;

import com.example.speech.web.clients.SpeechToTextClient;
import com.example.speech.web.dto.WhisperResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients
public class ListenerApplication implements CommandLineRunner {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private SpeechToTextClient sttClient;

    public static void main(String[] args) {
        SpringApplication.run(ListenerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        File file = new File("/home/abobus/Downloads/Russian_sayings.ogg.mp3");
//        byte[] response = sttClient.postAudioFile(file);
//
//        try {
//            WhisperResponse whisperResponse = OBJECT_MAPPER.readValue(response, WhisperResponse.class);
//            System.out.println(whisperResponse.getResults().get(0).getTranscript());
//        } catch (IOException e) {
//            System.out.println("PARSING JSON ERROR");
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//        }
    }
}
