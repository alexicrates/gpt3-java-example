package com.example.gpt3javaexample.test;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;

public class Main {
    public static void main(String[] args) {
        OpenAiService service = new OpenAiService("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");

        CompletionRequest request = CompletionRequest.builder()
                .prompt("Hello there")
                .model("text-ada-001")
                .maxTokens(1000)
                .build();

        service.createCompletion(request).getChoices().forEach(o -> System.out.println(o.getText()));
    }
}
