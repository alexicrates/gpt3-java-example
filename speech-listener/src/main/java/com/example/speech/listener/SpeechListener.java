package com.example.speech.listener;


import com.example.speech.listener.detectors.SpeechDetector;
import com.example.speech.listener.detectors.TriggerWordDetector;
import com.example.speech.listener.streamer.AudioFilesUtils;
import com.example.speech.listener.streamer.AudioStreamerRunnable;
import com.example.speech.web.clients.GptApiClient;
import com.example.speech.web.clients.SpeechToTextClient;
import com.example.speech.web.dto.WhisperResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static com.example.speech.listener.streamer.AudioFilesUtils.mergeFiles;
@Service
public class SpeechListener {
    public static String SPEECH_FILE = "only_speech.wav";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final AudioStreamerRunnable audioStreamerRunnable = new AudioStreamerRunnable();
    private final AudioFilesUtils audioFilesUtils = new AudioFilesUtils();
    private final ArrayList<File> files = new ArrayList<>();
    private final ArrayList<AudioInputStream> audioInputStreams = new ArrayList<>();

    private Thread thread;
    private String mergeFilePath = "";

    private final SpeechDetector speechDetector;
    private final TriggerWordDetector triggerWordDetector;
    private final SpeechToTextClient sttClient;
    private final GptApiClient gptApiClient;
    @Autowired
    public SpeechListener(SpeechDetector speechDetector,
                          TriggerWordDetector triggerWordDetector,
                          SpeechToTextClient sttClient, GptApiClient gptApiClient) {
        this.speechDetector = speechDetector;
        this.triggerWordDetector = triggerWordDetector;
        this.sttClient = sttClient;
        this.gptApiClient = gptApiClient;
    }

    @PostConstruct
    public void start(){
        startAudioStreaming();
    }
    @PreDestroy
    public void destroy(){
        cleanup();
    }

    public void startAudioStreaming(){
        thread = new Thread(audioStreamerRunnable);
        thread.start();
    }

    @Scheduled(fixedDelay = 1000)
    public void listen() {
        try {
            System.out.println("listen start");

            audioStreamerRunnable.getIsBusy().set(false);
            getSpeechSamples(1);
            audioStreamerRunnable.getIsBusy().set(true);

            System.out.println("listen end");

            mergeFilePath = mergeFiles(files, ".wav", AudioFileFormat.Type.WAVE);

            Objects.requireNonNull(mergeFilePath);
            speechDetector.isSpeech(mergeFilePath, true);

            byte[] responseBytes = sttClient.postAudioFile(new File(SPEECH_FILE));
            WhisperResponse whisperResponse = null;

            try {
                whisperResponse = OBJECT_MAPPER.readValue(responseBytes, WhisperResponse.class);
                System.out.println("Transcript: " + whisperResponse.getResults().get(0).getTranscript());
            } catch (IOException e) {
                System.out.println("CAN'T PARSE JSON");
                e.printStackTrace();
                whisperResponse = null;
            }

            cleanup();

            if (whisperResponse != null) {
                String gptResponse = gptApiClient.sendRequest(whisperResponse.getResults().get(0).getTranscript(), true);
                System.out.println("GPT Response: " + gptResponse);
            }

        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            cleanup();
        }
    }

    public void getSpeechSamples(int maxSilenceSamples) throws InterruptedException, IOException {
        boolean end = false;

        while (!end){
            Thread.sleep(3000);
            AudioInputStream audioInputStream = audioStreamerRunnable.getNewAudioInputStream();
            String fileName = audioFilesUtils.saveToFile("sound", AudioFileFormat.Type.WAVE, audioInputStream).getName();

            if (triggerWordDetector.isTriggerWordDetected(fileName)){
                System.out.println("СЛУШАЮ");

                int speech_samples = 0;
                int silence_samples = 0;

                while (speech_samples == 0 || silence_samples < maxSilenceSamples){
                    Thread.sleep(1000);
                    audioInputStream = audioStreamerRunnable.getNewAudioInputStream();
                    fileName = audioFilesUtils.saveToFile("sound", AudioFileFormat.Type.WAVE, audioInputStream).getName();

                    if (speechDetector.isSpeech(fileName, false)) {
                        speech_samples++;
                        System.out.println("speech detected");
                        audioInputStreams.add(audioInputStream);
                        files.add(new File(fileName));
                    } else {
                        System.out.println(".");
                        silence_samples++;
                    }
                }

                end = true;
            }
            else {
                System.out.println(".");
            }
        }
    }

    private void cleanup(){
        File currentDir = new File(".");
        for (File file : currentDir.listFiles()) {
            if (file.getName().contains(".wav")){
                file.delete();
            }
        }

        files.forEach(File::delete);
        files.clear();
        audioInputStreams.clear();
    }
}
