package com.example.speech.audio.listener;

import com.example.speech.audio.detectors.SpeechDetector;
import com.example.speech.audio.detectors.TriggerWordDetector;
import com.example.speech.audio.streamer.AudioFilesUtils;
import com.example.speech.audio.streamer.AudioStreamerRunnable;
import com.example.speech.web.clients.GuiClient;
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
    private final GuiClient guiClient;

    @Autowired
    public SpeechListener(SpeechDetector speechDetector,
                          TriggerWordDetector triggerWordDetector,
                          GuiClient guiClient) {
        this.speechDetector = speechDetector;
        this.triggerWordDetector = triggerWordDetector;
        this.guiClient = guiClient;
    }

    @PostConstruct
    public void start(){
        Thread thread = new Thread(audioStreamerRunnable);
        thread.start();
    }

    public ArrayList<File> getTempSpeechAudioFiles(int maxSilenceSamples, int sampleSizeInMillis) throws InterruptedException, IOException {
        boolean end = false;
        files.clear();

        while (true){
            Thread.sleep(1000);
            AudioInputStream audioInputStream = audioStreamerRunnable.getNewAudioInputStream();
            String fileName = audioFilesUtils.saveToFile("sound", AudioFileFormat.Type.WAVE, audioInputStream).getName();

            if (triggerWordDetector.isTriggerWordDetected(fileName)){
                recordSpeech(maxSilenceSamples, sampleSizeInMillis);
                break;
            }
            else {
                System.out.println("Trigger word is not detected");
            }
        }

        return files;
    }

    private void recordSpeech(int maxSilenceSamples, int sampleSizeInMillis) throws InterruptedException, IOException {
        int silenceSamples = 0;

        System.out.println("Recording your speech");
        guiClient.startRecord();
        while (silenceSamples < maxSilenceSamples){
            Thread.sleep(sampleSizeInMillis);
            AudioInputStream audioInputStream = audioStreamerRunnable.getNewAudioInputStream();
            File audioFile = audioFilesUtils.saveToFile("sound", AudioFileFormat.Type.WAVE, audioInputStream);

            if (speechDetector.isSpeech(audioFile.getName(), false)) {
                silenceSamples = 0;
                files.add(audioFile);
                System.out.println("Speech sample is recorded");
            } else {
                System.out.println("Speech sample is not recorded");
                silenceSamples++;
            }
        }
        guiClient.endRecord();
    }

    public void turnMicro(boolean turned){
        audioStreamerRunnable.setTurned(turned);
        System.out.println("Recording micro: " + turned);
    }
}
