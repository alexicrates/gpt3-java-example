package com.example.speech.audio.listener;

import com.example.speech.audio.detectors.SpeechDetector;
import com.example.speech.audio.detectors.SphinxTriggerWordDetector;
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
public class SpeechListenerRecorder {
    private final AudioStreamerRunnable audioStreamerRunnable = new AudioStreamerRunnable();
    private final AudioFilesUtils audioFilesUtils = new AudioFilesUtils();
    private final ArrayList<File> files = new ArrayList<>();

    private final SpeechDetector speechDetector;
    private final GuiClient guiClient;
    private final SphinxTriggerWordDetector sphinxTriggerWordDetector;
    private boolean ifListening = true;

    @Autowired
    public SpeechListenerRecorder(SpeechDetector speechDetector,
                                  GuiClient guiClient,
                                  SphinxTriggerWordDetector sphinxTriggerWordDetector) {
        this.speechDetector = speechDetector;
        this.guiClient = guiClient;
        this.sphinxTriggerWordDetector = sphinxTriggerWordDetector;
    }

    @PostConstruct
    public void start(){
        Thread thread = new Thread(audioStreamerRunnable);
        thread.start();
    }

    public ArrayList<File> getTempSpeechAudioFiles(int maxSilenceSamples, int sampleSizeInMillis) throws InterruptedException, IOException {
        files.clear();

        if (ifListening) {
            boolean isTriggered   = sphinxTriggerWordDetector.waitForTriggerWord();

            if (isTriggered){
                recordSpeech(maxSilenceSamples, sampleSizeInMillis);
            }
        }

        return files;
    }

    private void recordSpeech(int maxSilenceSamples, int sampleSizeInMillis) throws InterruptedException, IOException {
        int silenceSamples = 0;

        System.out.println("Recording your speech");

        turnMicro(true);

        while (silenceSamples < maxSilenceSamples){
            Thread.sleep(sampleSizeInMillis);
            File audioFile = audioFilesUtils.saveToFile("sound",
                    AudioFileFormat.Type.WAVE,
                    audioStreamerRunnable.getNewAudioInputStream());

            if (speechDetector.isSpeech(audioFile.getName(), false)) {
                silenceSamples = 0;
                files.add(audioFile);
                System.out.println("Speech sample is recorded");
            } else {
                System.out.println("Speech sample is not recorded");
                silenceSamples++;
            }
        }

        turnMicro(false);
    }

    public void turnMicro(boolean shouldListen){
        this.ifListening = shouldListen;
        sphinxTriggerWordDetector.setListening(shouldListen);
        audioStreamerRunnable.setListening(shouldListen);

        try {
            String s = shouldListen ? guiClient.startRecord() : guiClient.endRecord();
        } catch (Exception e) {
            System.out.println("No gui is present");
        }

        System.out.println("Recording micro: " + shouldListen);
    }
}
