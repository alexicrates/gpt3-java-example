package com.example.gpt3javaexample.listener;

import soundapi.WaveDataUtil;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.File;
import java.io.IOException;

public class AudioTest {

    public static void main(String[] args) throws InterruptedException, IOException {
        WaveDataUtil waveDataUtil = new WaveDataUtil();

        AudioStreamerRunnable audioStreamerRunnable = new AudioStreamerRunnable();
        int blockSize = audioStreamerRunnable.getBlock_size();
        AudioFormat format = audioStreamerRunnable.getFormat();

        Thread thread = new Thread(audioStreamerRunnable);
        thread.start();


        for (int i = 0; i < 5; i++) {
            Thread.sleep(1000);
            AudioInputStream audioInputStream = audioStreamerRunnable.getAudioInputStream();
            waveDataUtil.saveToFile("sound", AudioFileFormat.Type.WAVE, audioInputStream);
            boolean delete = new File("sound.wav").delete();
            System.out.println(delete);
        }
        Thread.sleep(1000);
        AudioInputStream audioInputStream = audioStreamerRunnable.getAudioInputStream();
        waveDataUtil.saveToFile("sound", AudioFileFormat.Type.WAVE, audioInputStream);
    }
}
