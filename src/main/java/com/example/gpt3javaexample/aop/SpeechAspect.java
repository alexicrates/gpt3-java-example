package com.example.gpt3javaexample.aop;

import com.example.gpt3javaexample.util.SoundPlayer;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Aspect
@Component
public class SpeechAspect {

    private final SoundPlayer soundPlayer = new SoundPlayer();

    @AfterReturning(pointcut = "@annotation(ToSpeech)", returning = "text")
    @Async
    public void toSpeech(String text){
        try {
            soundPlayer.play(text);
        } catch (IOException | InterruptedException e) {
            System.out.println("CAN'T SPEAK!!!!!!!!!!!!!!!!!!!");
            System.out.println(e.fillInStackTrace().toString());
        }
    }
}
