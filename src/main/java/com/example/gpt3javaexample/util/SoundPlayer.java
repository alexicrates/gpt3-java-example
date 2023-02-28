package com.example.gpt3javaexample.util;

import com.example.gpt3javaexample.MakeSound;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SoundPlayer {

    private MakeSound makeSound = new MakeSound();

    public void play(String text) throws IOException, InterruptedException {
        File file = File.createTempFile("temp", ".txt");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(text.getBytes());
        fileOutputStream.close();

        Process exec = Runtime.getRuntime().exec("python3 /home/alexicrates/IdeaProjects/gpt3-java-example/gpt3-java-example/tts.py ".concat(file.getAbsolutePath()));
        boolean res = exec.waitFor(10, TimeUnit.SECONDS);
        if (res)
            new MakeSound().playSound("test.wav");
    }
}
