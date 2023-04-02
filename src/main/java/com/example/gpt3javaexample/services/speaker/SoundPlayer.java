package com.example.gpt3javaexample.services.speaker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SoundPlayer {

    private final MakeSound makeSound = new MakeSound();

    public void play(String text) throws IOException, InterruptedException {
        File file = File.createTempFile("temp", ".txt");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(text.getBytes());
        fileOutputStream.close();

        Process exec = Runtime.getRuntime().exec("python3 ./python_scripts/tts.py ".concat(file.getAbsolutePath()));
        int res = exec.waitFor();
        if (res == 0) {
            makeSound.playSound("test.wav");
        } else {
            exec.errorReader().lines().forEach(System.out::println);
        }
    }
}
