package com.example.gpt3javaexample.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SoundPlayer {

    private final MakeSound makeSound = new MakeSound();

    public void play(String text) throws IOException, InterruptedException {
        File file = File.createTempFile("temp", ".txt");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(text.getBytes());
        fileOutputStream.close();

        Process exec = Runtime.getRuntime().exec("python3 ./python_scripts/tts.py ".concat(file.getAbsolutePath()));
        boolean res = exec.waitFor(10, TimeUnit.SECONDS);
        if (res)
            makeSound.playSound("test.wav");
    }
}
