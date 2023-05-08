package com.example.web.controllers;

import com.example.gui.MainFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class GUIController {

    @Autowired
    private MainFrame mainFrame;

    @GetMapping("/record/start")
    public String startRecord() throws IOException {
        mainFrame.setRecording(true);
        return "recording started";
    }

    @GetMapping("/record/end")
    public String endRecord() throws IOException {
        mainFrame.setRecording(false);
        return "recording ended";
    }

    @GetMapping(value = "/message/user", params = {"text"})
    public String appendUserMessage(@RequestParam("text") String text){
        mainFrame.appendMessage(MainFrame.Role.YOU, text);
        return "appended";
    }

    @GetMapping(value = "/message/bot", params = {"text"})
    public String appendBotMessage(@RequestParam("text") String text){
        mainFrame.appendMessage(MainFrame.Role.BOT, text);
        return "appended";
    }
}
