package com.example.gpt3javaexample.controllers;

import com.example.gpt3javaexample.services.GPTService;
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

    @GetMapping(value = "/", params = "prompt")
    String request(@RequestParam("prompt") String prompt){
        return gptService.doRequest(prompt);
    }

}
