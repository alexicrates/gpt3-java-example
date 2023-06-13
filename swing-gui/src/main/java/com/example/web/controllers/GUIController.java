package com.example.web.controllers;

import com.example.gui.MainFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GUIController {

    @Autowired
    private MainFrame mainFrame;

    @GetMapping("/record/start")
    public String startRecord() {
        mainFrame.setRecording(true);
        return "recording started";
    }

    @GetMapping("/record/end")
    public String endRecord() {
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

    @GetMapping(value = "/error/{message}")
    public void showErrorMessage(@PathVariable("message") String message){
        mainFrame.showErrorMessage(message);
    }
}
