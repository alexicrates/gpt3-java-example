package com.example.gpt3javaexample.utils.stt;

import java.io.IOException;
import java.util.Optional;

public class SpeechToTextConverter {

    public static String convert(String pathString) throws IOException {
        Process exec = Runtime.getRuntime().exec("whisper "+ pathString + " --language Russian --fp16 False --model base --threads 2");;
        Optional<String> result = exec.inputReader().lines().reduce(String::concat);
        return result.orElse(null);
    }

}
