package com.example.gpt3javaexample.test;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;

import java.io.*;
import java.time.Duration;
import java.util.Scanner;

public class GptClient {

    private StringBuilder logs;
    private OpenAiService service;

    public GptClient() {
        logs = new StringBuilder();
        service = new OpenAiService("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", Duration.ofMinutes(5));
    }

    public void startChat() {

        Scanner scanner = new Scanner(System.in);

        while(true){
            System.out.print("Input: ");
            String input = scanner.nextLine();
            System.out.println();

            if (input.equals("q")) break;

            logs.append("Input:\n").append(input).append("\n\n").append("Output: ");

            CompletionRequest request = CompletionRequest.builder()
                    .prompt(logs.toString())
                    .model("text-davinci-003")
                    .maxTokens(2000)
                    .build();

            String output = service.createCompletion(request).getChoices().stream()
                    .map(CompletionChoice::getText)
                    .reduce(String::concat)
                    .get();

            System.out.println("Output: " + output + "\n");
            logs.append(output).append("\n\n");
        }

        try {
            saveLogs();
        } catch (IOException e) {
            System.out.println("FAILED TO SAVE");
        }
    }

    public void saveLogs() throws IOException {
        File file = new File("logs.txt");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(logs.toString().getBytes());
    }
}
