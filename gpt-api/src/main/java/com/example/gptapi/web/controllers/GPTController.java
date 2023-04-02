package com.example.gptapi.web.controllers;

import com.example.gptapi.services.gpt.GPTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GPTController {

    private final GPTService gptService;

    @Autowired
    public GPTController(GPTService gptService) {
        this.gptService = gptService;
    }

    @GetMapping(value = "/", params = {"prompt", "new_chat"})
    String request(@RequestParam("prompt") String prompt,
                   @RequestParam("new_chat") Boolean newChat){
        return gptService.doRequest(prompt, newChat);
    }

}
