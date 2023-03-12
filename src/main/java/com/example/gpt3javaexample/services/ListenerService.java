package com.example.gpt3javaexample.services;

import com.example.gpt3javaexample.utils.SpeechDetector;
import com.example.gpt3javaexample.utils.listener.AudioStreamerRunnable;
import com.example.gpt3javaexample.utils.listener.AudioTest;
import com.example.gpt3javaexample.utils.soundapi.WaveDataUtil;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.example.gpt3javaexample.Gpt3JavaExampleApplication.ONLY_SPEECH;
import static com.example.gpt3javaexample.utils.listener.AudioTest.apply;
import static com.example.gpt3javaexample.utils.listener.AudioTest.merge;

@Service
public class ListenerService {

    private final AudioStreamerRunnable audioStreamerRunnable = new AudioStreamerRunnable();
    private final WaveDataUtil waveDataUtil = new WaveDataUtil();
    private Thread thread;

    public void startListening() throws InterruptedException, IOException {
        thread = new Thread(audioStreamerRunnable);
        thread.start();
        Thread.sleep(500);
        listen();
    }

    private void listen() throws InterruptedException, IOException {
        List<AudioInputStream> audioInputStreams = new ArrayList<>();
        List<File> files = new ArrayList<>();

        while (true) {
//            Thread.sleep(100);
            AudioInputStream audioInputStream = audioStreamerRunnable.getAudioInputStream();
            String fileName = waveDataUtil.saveToFile("sound", AudioFileFormat.Type.WAVE, audioInputStream).getName();

            if (SpeechDetector.isSpeech(fileName)){
                System.out.println("speech detected");
                audioInputStreams.add(audioInputStream);
                files.add(new File(fileName));
                new File(ONLY_SPEECH).delete();

                audioInputStream = audioStreamerRunnable.getAudioInputStream();
                fileName = waveDataUtil.saveToFile("sound", AudioFileFormat.Type.WAVE, audioInputStream).getName();

                while (SpeechDetector.isSpeech(fileName)){
//                    Thread.sleep(100);
                    System.out.println("speech detected");
                    audioInputStreams.add(audioInputStream);
                    files.add(new File(fileName));
                    new File(ONLY_SPEECH).delete();

                    audioInputStream = audioStreamerRunnable.getAudioInputStream();
                    fileName = waveDataUtil.saveToFile("sound", AudioFileFormat.Type.WAVE, audioInputStream).getName();
                }
            }
            else {
                System.out.println(".");
                new File(fileName).delete();
            }

            if (files.isEmpty())
                continue;

            String mergeFile;

            if (files.size() == 1){
                mergeFile = UUID.randomUUID() + ".wav";
                AudioSystem.write(apply(files.get(0)), AudioFileFormat.Type.WAVE, new File(mergeFile));
            }
            else {
                mergeFile = merge(files.stream().map(AudioTest::apply).toList(), ".wav", AudioFileFormat.Type.WAVE);

                files.forEach(File::delete);
                files.clear();
                audioInputStreams.clear();
            }

            SpeechDetector.isSpeech(mergeFile);
            System.out.println("recognizing your speech");
            Process exec = Runtime.getRuntime().exec("whisper "+ ONLY_SPEECH + " --language Russian --fp16 False --model base --threads 2");
            new File(mergeFile).delete();

            if (exec.waitFor(10, TimeUnit.SECONDS)){
                byte[] requestBytes = new FileInputStream(ONLY_SPEECH.replace(".wav", ".txt")).readAllBytes();
                String request = new String(requestBytes);
                System.out.println("You said: " + request);
//                System.out.println("Sending your request to GPT");
//                String response = client.sendRequest(request, false);
//                System.out.println("GPT response: " + response);
            }
            else {
                System.out.println("CAN'T RECOGNIZE");
                exec.errorReader().lines().forEach(System.out::println);
            }
        }
    }

}
