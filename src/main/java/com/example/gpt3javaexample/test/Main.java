package com.example.gpt3javaexample.test;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;

import java.time.Duration;

public class Main {
    public static void main(String[] args) {
//        OpenAiService service = new OpenAiService("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", Duration.ofMinutes(5));
//
//        CompletionRequest request = CompletionRequest.builder()
//                .prompt("How are you doing?")
//                .model("text-davinci-003")
//                .maxTokens(2000)
//                .build();
//
//        String output = service.createCompletion(request).getChoices().stream()
//                .map(CompletionChoice::getText)
//                .reduce(String::concat)
//                .get();
//
//        System.out.println(output);


        GptClient client = new GptClient();
        client.startChat();
    }
}
