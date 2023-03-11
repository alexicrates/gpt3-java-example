package com.example.gpt3javaexample;

import ch.qos.logback.core.net.server.Client;
import com.example.gpt3javaexample.client.ListenerClient;
import com.example.gpt3javaexample.utils.SpeechDetector;
import com.example.gpt3javaexample.utils.listener.AudioStreamerRunnable;
import com.example.gpt3javaexample.utils.listener.AudioTest;
import com.example.gpt3javaexample.utils.soundapi.WaveDataUtil;
import com.example.gpt3javaexample.utils.speaker.MakeSound;
import com.example.gpt3javaexample.utils.speaker.SoundPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.gpt3javaexample.utils.listener.AudioTest.apply;
import static com.example.gpt3javaexample.utils.listener.AudioTest.merge;

@SpringBootApplication
@EnableFeignClients
public class Gpt3JavaExampleApplication {


    static ListenerClient client = null;

    @Autowired
    public Gpt3JavaExampleApplication(ListenerClient client) {
        this.client = client;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(Gpt3JavaExampleApplication.class, args);
//        Process exec = Runtime.getRuntime().exec("whisper test.wav --language Russian");
//        System.out.println(new String(exec.getInputStream().readAllBytes()));


        //////////////////////////////////////////////////////////////////////////////////////
        while (true) {
            WaveDataUtil waveDataUtil = new WaveDataUtil();

            AudioStreamerRunnable audioStreamerRunnable = new AudioStreamerRunnable();
            Thread thread = new Thread(audioStreamerRunnable,"streamer");
            thread.start();

            Thread.sleep(500);

            List<AudioInputStream> audioInputStreams = new ArrayList<>();
            List<File> files = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                Thread.sleep(100);
                AudioInputStream audioInputStream = audioStreamerRunnable.getAudioInputStream();
                String fileName = waveDataUtil.saveToFile("sound", AudioFileFormat.Type.WAVE, audioInputStream).getName();

                if (SpeechDetector.isSpeech(fileName)){
                    System.out.println("speech detected");
                    audioInputStreams.add(audioInputStream);
                    files.add(new File(fileName));
                    new File("only_speech.wav").delete();
                }
                else {
                    System.out.println(".");
                    new File(fileName).delete();
                }
            }
            System.out.println("end");

            if (files.isEmpty())
                return;

            String mergeFile;

            if (files.size() == 1){
                mergeFile = UUID.randomUUID() + ".wav";
                AudioSystem.write(apply(files.get(0)), AudioFileFormat.Type.WAVE, new File(mergeFile));
            }
            else {
                mergeFile = merge(files.stream().map(AudioTest::apply).toList(), ".wav", AudioFileFormat.Type.WAVE);

                if (mergeFile == null) {
                    files.forEach(file -> file.delete());
                    return;
                }
            }

            SpeechDetector.isSpeech(mergeFile);
            new MakeSound().playSound("only_speech.wav");

            System.out.println("recognizing your speech");

            Process exec = Runtime.getRuntime().exec("whisper "+ "only_speech.wav" + " --language Russian --fp16 False --model small --threads 2");

            if (exec.waitFor() == 0){
                byte[] requestBytes = new FileInputStream("only_speech.wav.txt").readAllBytes();
                String request = new String(requestBytes);
                System.out.println("You said: " + request);
                System.out.println("Sending your request to GPT");
                String response = client.sendRequest(request, false);
                System.out.println("GPT response: " + response);
            }
            else {
                System.out.println("SPEECH RECOGNITION ERROR!!!");
                exec.errorReader().lines().forEach(System.out::println);
            }
        }
        //////////////////////////////////////////////////////////////////////////////////////


    }
}
