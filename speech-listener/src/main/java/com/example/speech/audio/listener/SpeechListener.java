package com.example.speech.audio.listener;

import com.example.speech.audio.detectors.SpeechDetector;
import com.example.speech.audio.detectors.TriggerWordDetector;
import com.example.speech.audio.streamer.AudioFilesUtils;
import com.example.speech.audio.streamer.AudioStreamerRunnable;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Service
public class SpeechListener {
    private final AudioStreamerRunnable audioStreamerRunnable = new AudioStreamerRunnable();
    private final AudioFilesUtils audioFilesUtils = new AudioFilesUtils();
    private final ArrayList<File> files = new ArrayList<>();

    private final SpeechDetector speechDetector;
    private final TriggerWordDetector triggerWordDetector;

    @Autowired
    public SpeechListener(SpeechDetector speechDetector,
                          TriggerWordDetector triggerWordDetector) {
        this.speechDetector = speechDetector;
        this.triggerWordDetector = triggerWordDetector;
    }

    @PostConstruct
    public void start(){
        Thread thread = new Thread(audioStreamerRunnable);
        thread.start();
    }

    public ArrayList<File> getSpeechSamples(int maxSilenceTimeInMillis) throws InterruptedException, IOException {
        boolean end = false;
        files.clear();

        while (!end){
            Thread.sleep(1000);
            AudioInputStream audioInputStream = audioStreamerRunnable.getNewAudioInputStream();
            String fileName = audioFilesUtils.saveToFile("sound", AudioFileFormat.Type.WAVE, audioInputStream).getName();

            if (triggerWordDetector.isTriggerWordDetected(fileName)){
                int speechSamples = 0;
                int silenceSamples = 0;
                int sampleSizeInMillis = 100;

                System.out.println("Recording your speech");
                while (speechSamples == 0 || silenceSamples < maxSilenceTimeInMillis / sampleSizeInMillis){
                    Thread.sleep(sampleSizeInMillis);
                    audioInputStream = audioStreamerRunnable.getNewAudioInputStream();
                    fileName = audioFilesUtils.saveToFile("sound", AudioFileFormat.Type.WAVE, audioInputStream).getName();

                    if (speechDetector.isSpeech(fileName, false)) {
                        speechSamples++;
                        files.add(new File(fileName));
                        System.out.println("Speech sample is recorded");
                    } else {
                        System.out.println(".");
                        silenceSamples++;
                    }
                }

                end = true;
            }
            else {
                System.out.println("Trigger word is not detected");
            }
        }

        return files;
    }
}
