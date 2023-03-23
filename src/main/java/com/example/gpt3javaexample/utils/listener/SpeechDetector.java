package com.example.gpt3javaexample.utils.listener;

import java.io.IOException;

public class SpeechDetector {

    public static boolean isSpeech(String fileName, boolean saveSpeechToFile) throws IOException, InterruptedException {

        StringBuilder args = new StringBuilder();
        args.append(" -f" + fileName + " ");
        args.append(" -s " + (saveSpeechToFile ? "1" : "0") + " ");

        Process exec = Runtime.getRuntime().exec("python3 ./python_scripts/speech_detection.py" + args);
        int i = exec.waitFor();

        if (i != 0) return false;
        else return exec.inputReader().lines().anyMatch(string -> string.contains("speech"));
    }

}
