package com.example.gpt3javaexample.utils;

public class WhisperAnswerSanitizer {
    public static String sanitize(String text){
        return text.replaceAll("\\[.*]", "");
    }

}
