package com.example.gpt3javaexample.services;

import com.example.gpt3javaexample.utils.WhisperAnswerSanitizer;
import com.example.gpt3javaexample.utils.soundapi.AudioStreamerRunnable;
import com.example.gpt3javaexample.utils.soundapi.WaveDataUtil;
import com.example.gpt3javaexample.utils.speaker.MakeSound;
import com.example.gpt3javaexample.utils.stt.SpeechToTextConverter;
import com.example.gpt3javaexample.web.client.ListenerClient;
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

import static com.example.gpt3javaexample.utils.soundapi.AudioFilesUtils.mergeFiles;

@Service
public class SpeechListener {
    public static String ONLY_SPEECH = "only_speech.wav";

    private final AudioStreamerRunnable audioStreamerRunnable = new AudioStreamerRunnable();
    private final WaveDataUtil waveDataUtil = new WaveDataUtil();
    private final ArrayList<File> files = new ArrayList<>();
    private final ArrayList<AudioInputStream> audioInputStreams = new ArrayList<>();

    private Thread thread;
    private String mergeFilePath = "";

    private final SpeechDetector speechDetector;
    private final TriggerWordDetector triggerWordDetector;
    private final ListenerClient client;

    @Autowired
    public SpeechListener(SpeechDetector speechDetector, TriggerWordDetector triggerWordDetector, ListenerClient client) {
        this.speechDetector = speechDetector;
        this.triggerWordDetector = triggerWordDetector;
        this.client = client;
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
            new MakeSound().playSound(ONLY_SPEECH);

            String transcription = WhisperAnswerSanitizer.sanitize(SpeechToTextConverter.convert(ONLY_SPEECH));
            cleanup();

            System.out.println("transcription: ".toUpperCase() + transcription);
//            client.sendRequest(transcription, true);

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
            String fileName = waveDataUtil.saveToFile("sound", AudioFileFormat.Type.WAVE, audioInputStream).getName();

            if (triggerWordDetector.isTriggerWordDetected(fileName)){
                System.out.println("СЛУШАЮ");

                int speech_samples = 0;
                int silence_samples = 0;

                while (speech_samples == 0 || silence_samples < maxSilenceSamples){
                    Thread.sleep(3000);
                    audioInputStream = audioStreamerRunnable.getNewAudioInputStream();
                    fileName = waveDataUtil.saveToFile("sound", AudioFileFormat.Type.WAVE, audioInputStream).getName();

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
                System.out.println("ДУМАЮ");
            }
            else {
                System.out.println(".");
            }
        }
    }

    private void cleanup(){
        File onlySpeechFile = new File(ONLY_SPEECH);
        File mergeFile = new File(mergeFilePath);

        if (onlySpeechFile.exists()) {
            files.add(onlySpeechFile);
        }
        if (mergeFile.exists()) {
            files.add(mergeFile);
        }

        files.forEach(File::delete);
        files.clear();
        audioInputStreams.clear();
    }
}
