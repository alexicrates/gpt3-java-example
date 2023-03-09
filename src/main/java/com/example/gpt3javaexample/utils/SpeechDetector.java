package com.example.gpt3javaexample.utils;

import java.io.IOException;

public class SpeechDetector {

    public static boolean isSpeech(String fileName) throws IOException, InterruptedException {
        Process exec = Runtime.getRuntime().exec("python3 ./python_scripts/speech_detection.py -f ".concat(fileName));
        int i = exec.waitFor();

        if (i != 0) return false;
        else return exec.inputReader().lines().anyMatch(string -> string.contains("speech"));
    }

}
