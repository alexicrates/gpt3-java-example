package com.example.gptapi.services.gpt;

import com.example.gptapi.aop.SaveToLogs;

import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GPTService {

    @Value("${openai.max_tokens}")
    private int MAX_TOKENS;

    @Value("${openai.model:text-davinci-003}")
    private String MODEL;

    private final OpenAiService service;
    private final StringBuilder chatHistory;

    private final static String precondition =
            "Ты голосовой ассистент по имени Алеся, который специализируется на консультации по сельскому хозяйству. " +
                    "Далее Input - это сообщение пользователя, а Output - твой ответ. " +
                    "В сообщениях пользователя возможны любые ошибки из-за неточности транскрипции. " +
                    "Если в сообщении пользователя бессмыслица, то дай понять это";

    @Autowired
    public GPTService(OpenAiService service) {
        this.service = service;
        this.chatHistory = new StringBuilder();

        chatHistory.append(precondition);
    }

    @SaveToLogs
    public String doRequest(String prompt, Boolean newChat){

        if (newChat){
            clearHistory();
        }

        chatHistory.append("\nInput: ").append(prompt).append("\nOutput: ");

        CompletionRequest request = CompletionRequest.builder()
                .prompt(chatHistory.toString())
                .model(MODEL)
                .maxTokens(MAX_TOKENS)
                .build();

        String response = service.createCompletion(request).getChoices().stream()
                .map(CompletionChoice::getText)
                .reduce(String::concat)
                .orElse("I don't know what to say");

        chatHistory.append(response).append("\n");

        return response;
    }

    public void clearHistory(){
        chatHistory.delete(0, chatHistory.length());
        chatHistory.append(precondition);
    }
}
