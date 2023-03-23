package com.example.gpt3javaexample.services;

import com.example.gpt3javaexample.utils.listener.AudioStreamerRunnable;
import com.example.gpt3javaexample.utils.soundapi.WaveDataUtil;
import com.example.gpt3javaexample.utils.speaker.MakeSound;
import com.example.gpt3javaexample.utils.stt.SpeechToTextConverter;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.gpt3javaexample.utils.listener.AudioFilesUtils.mergeFiles;
import static com.example.gpt3javaexample.utils.listener.SpeechDetector.isSpeech;

@Service
public class SpeechListener {
    public static String ONLY_SPEECH = "only_speech.wav";

    private final AudioStreamerRunnable audioStreamerRunnable = new AudioStreamerRunnable();
    private final WaveDataUtil waveDataUtil = new WaveDataUtil();
    private final ArrayList<File> files = new ArrayList<>();
    private final ArrayList<AudioInputStream> audioInputStreams = new ArrayList<>();

    private Thread thread;
    private String mergeFile;

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
    public void listen() throws IOException, InterruptedException {

        System.out.println("listen start");

        audioStreamerRunnable.getIsBusy().set(false);
        getSpeechSamples(0);
        audioStreamerRunnable.getIsBusy().set(true);

        mergeFile = mergeFiles(files, ".wav", AudioFileFormat.Type.WAVE);
        isSpeech(mergeFile, true);

        new MakeSound().playSound(ONLY_SPEECH);

        String transcription = SpeechToTextConverter.convert(ONLY_SPEECH);
        System.out.println("listen end");
        System.out.println("transcription: ".toUpperCase() + transcription);

        cleanup();
    }

    public void getSpeechSamples(int maxSilenceSamples) throws InterruptedException, IOException {
        int silenceSamples = 0;
        boolean start = false;
        boolean end = false;

        while (!end){
            AudioInputStream audioInputStream = audioStreamerRunnable.getNewAudioInputStream();
            String fileName = waveDataUtil.saveToFile("sound", AudioFileFormat.Type.WAVE, audioInputStream).getName();

            if (isSpeech(fileName, false)){
                System.out.println("speech detected");
                audioInputStreams.add(audioInputStream);
                files.add(new File(fileName));

                start = true;
                silenceSamples = 0;
            }
            else {
                if (start) {
                    silenceSamples++;
                }
                end = (silenceSamples >= maxSilenceSamples) && start;

                System.out.println(".");
                new File(fileName).delete();
            }
        }
    }

    private void cleanup(){
        files.add(new File(ONLY_SPEECH));
        files.add(new File(mergeFile));
        files.forEach(File::delete);
        files.clear();
        audioInputStreams.clear();
    }
}
