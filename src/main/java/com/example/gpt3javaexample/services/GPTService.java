package com.example.gpt3javaexample.services;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GPTService {

    @Value("${openai.max_tokens}")
    private int MAX_TOKENS;

    @Value("${openai.model}")
    private String MODEL;

    private final OpenAiService service;

    @Autowired
    public GPTService(OpenAiService service) {
        this.service = service;
    }

    public String doRequest(String prompt){

        String input = "Input: " + prompt + "Output: ";

        CompletionRequest request = CompletionRequest.builder()
                .prompt(input)
                .model(MODEL)
                .maxTokens(MAX_TOKENS)
                .build();

        String response = service.createCompletion(request).getChoices().stream()
                .map(CompletionChoice::getText)
                .reduce(String::concat)
                .orElse("Hmmm.... I don't know what to say");

        return response;
    }

}
