package com.example.gpt3javaexample.aop;

import com.example.gpt3javaexample.utils.speaker.SoundPlayer;
import com.ibm.icu.text.Transliterator;
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
    public void toSpeech(String text){
        try {
            Transliterator toCyrillicTrans = Transliterator.getInstance("Latin-Cyrillic");
            soundPlayer.play(toCyrillicTrans.transliterate(text));
        } catch (IOException | InterruptedException e) {
            System.out.println("CAN'T SPEAK!!!!!!!!!!!!!!!!!!!");
            System.out.println(e.fillInStackTrace().toString());
        }
    }
}
