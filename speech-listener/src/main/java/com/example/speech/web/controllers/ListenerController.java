package com.example.speech.web.controllers;

import com.example.speech.audio.listener.SpeechListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ListenerController {

    @Autowired
    private SpeechListener speechListener;

    @GetMapping(value = "/micro/on" )
    public String microOn(){
        speechListener.turnMicro(true);
        return "turned on";
    }

    @GetMapping(value = "/micro/off")
    public String microOff(){
        speechListener.turnMicro(false);
        return "turned off";
    }

}
