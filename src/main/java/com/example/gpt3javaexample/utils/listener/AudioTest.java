package com.example.gpt3javaexample.utils.listener;

import com.example.gpt3javaexample.client.ListenerClient;
import com.example.gpt3javaexample.utils.SpeechDetector;
import com.example.gpt3javaexample.utils.soundapi.WaveDataUtil;
import com.example.gpt3javaexample.utils.speaker.MakeSound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AudioTest {

    public static void main(String[] args) throws InterruptedException, IOException {
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

        Process exec = Runtime.getRuntime().exec("whisper "+ "only_speech.wav" + " --language Russian --fp16 False --model small");

        if (exec.waitFor() == 0){
            byte[] requestBytes = new FileInputStream("only_speech.wav.txt").readAllBytes();
            String request = new String(requestBytes);
//            System.out.println("You said: " + request);
            args[0] = request;
        }
        else {
            System.out.println("SPEECH RECOGNITION ERROR!!!");
            exec.errorReader().lines().forEach(System.out::println);
        }

    }

    public static String merge(List<AudioInputStream> audioInputStreams, String fileType, AudioFileFormat.Type audioType) {
        UUID randomUUID = UUID.randomUUID();
        String response = randomUUID.toString().concat(fileType);
        AudioInputStream appendedFiles = null;
        if (audioInputStreams.size()==0) {
            return null;
        }
        if (audioInputStreams.size() == 1) {
            return null;
        }
        for (int i = 0; i< audioInputStreams.size() - 1; i++) {
            if (i==0) {
                appendedFiles = new AudioInputStream(
                        new SequenceInputStream(audioInputStreams.get(i), audioInputStreams.get(i+1)),
                        audioInputStreams.get(i).getFormat(),
                        audioInputStreams.get(i).getFrameLength() + audioInputStreams.get(i+1).getFrameLength());
                continue;
            }
            appendedFiles =
                    new AudioInputStream(
                            new SequenceInputStream(appendedFiles, audioInputStreams.get(i+1)),
                            appendedFiles.getFormat(),
                            appendedFiles.getFrameLength() + audioInputStreams.get(i+1).getFrameLength());
        }
        try {
            AudioSystem.write(appendedFiles,
                    audioType,
                    new File(response));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return response;
    }

    public static AudioInputStream apply(File file) {
        try {
            return AudioSystem.getAudioInputStream(file);
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
