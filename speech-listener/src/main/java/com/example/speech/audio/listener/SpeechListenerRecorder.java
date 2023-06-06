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

    int maxSilenceSamples = 2;
    int sampleSizeInMillis = 50;

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

    public ArrayList<File> getTempSpeechAudioFiles() throws InterruptedException, IOException {
        files.clear();

        if (ifListening) {
            boolean isTriggered = sphinxTriggerWordDetector.waitForTriggerWord();

            if (isTriggered){
                record();
            }
        }

        return files;
    }

    private void record() throws IOException, InterruptedException {
        activateRecording(true);
        recordSpeech(maxSilenceSamples, sampleSizeInMillis);
        activateRecording(false);
    }

    private void activateRecording(boolean shouldRecord){
        activateRecordIndicator(shouldRecord);
        audioStreamerRunnable.setListening(shouldRecord);
    }

    private void recordSpeech(int maxSilenceSamples, int sampleSizeInMillis) throws InterruptedException, IOException {
        int silenceSamples = 0;

        System.out.println("Recording your speech");

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
    }

    public void turnMicro(boolean shouldListen){
        this.ifListening = shouldListen;
        sphinxTriggerWordDetector.setListening(shouldListen);
        audioStreamerRunnable.setListening(shouldListen);
        System.out.println("Recording micro: " + shouldListen);
    }

    public void activateRecordIndicator(boolean isActive){
        try {
            String s = isActive ? guiClient.startRecord() : guiClient.endRecord();
            if (!s.contains("recording")){
                throw new Exception();
            }
        } catch (Exception e) {
            System.out.println("No gui is present");
        }

    }
}
