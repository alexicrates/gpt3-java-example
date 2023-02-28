package com.example.gpt3javaexample;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.translate.TranslateException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class Gpt3JavaExampleApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(Gpt3JavaExampleApplication.class, args);
    }
}
