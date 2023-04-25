package com.example.speech.audio.detectors;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SpeechDetector {

    public boolean isSpeech(String fileName, boolean saveSpeechToFile) throws IOException, InterruptedException {

        String args = " -f" + fileName + " " + " -s " + (saveSpeechToFile ? "1" : "0") + " ";

        Process exec = Runtime.getRuntime().exec("python3 ./python_scripts/speech_detection.py" + args);
        int i = exec.waitFor();

        if (i != 0) return false;
        else return exec.inputReader().lines().anyMatch(string -> string.contains("speech"));
    }
}
