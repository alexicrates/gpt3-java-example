package com.example.speech.web.controllers;

import com.example.speech.audio.listener.SpeechListenerRecorder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ListenerController {

    @Autowired
    private SpeechListenerRecorder speechListenerRecorder;

    @GetMapping(value = "/micro/on" )
    public String microOn(){
        speechListenerRecorder.turnMicro(true);
        return "turned on";
    }

    @GetMapping(value = "/micro/off")
    public String microOff(){
        speechListenerRecorder.turnMicro(false);
        return "turned off";
    }

}
