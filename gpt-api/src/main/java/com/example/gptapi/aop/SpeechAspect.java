package com.example.gptapi.aop;

import com.example.gptapi.services.speaker.SoundPlayer;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Aspect
@Component
public class SpeechAspect {

    private final SoundPlayer soundPlayer = new SoundPlayer();

    @AfterReturning(pointcut = "@annotation(ToSpeech)", returning = "text")
    public void toSpeech(String text){
        try {
            soundPlayer.play(Translit.translit(text));
        } catch (IOException | InterruptedException e) {
            System.out.println("CAN'T SPEAK!!!!!!!!!!!!!!!!!!!");
            System.out.println(e.fillInStackTrace().toString());
        }
    }
}
