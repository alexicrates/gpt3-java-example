package com.example.gpt3javaexample.utils.listener;

import com.example.gpt3javaexample.utils.SpeechDetector;
import com.example.gpt3javaexample.utils.soundapi.WaveDataUtil;
import com.example.gpt3javaexample.utils.speaker.MakeSound;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AudioTest {

    public static void main(String[] args) throws InterruptedException, IOException {
        WaveDataUtil waveDataUtil = new WaveDataUtil();

        AudioStreamerRunnable audioStreamerRunnable = new AudioStreamerRunnable();
        Thread thread = new Thread(audioStreamerRunnable);
        thread.start();

        Thread.sleep(500);

        AudioInputStream speechAudioStream = null;
        List<AudioInputStream> audioInputStreams = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Thread.sleep(3000);
            AudioInputStream audioInputStream = audioStreamerRunnable.getAudioInputStream();
            String file = waveDataUtil.saveToFile("sound", AudioFileFormat.Type.WAVE, audioInputStream).getName();

            if (SpeechDetector.isSpeech(file)){
                System.out.println("speech detected");
                audioInputStreams.add(audioInputStream);

                if (speechAudioStream == null){
                    System.out.println("0");
                    speechAudioStream = new AudioInputStream(
                            audioInputStream,
                            audioInputStream.getFormat(),
                            audioInputStream.getFrameLength());
                    break;
                }
                else {
                    System.out.println("1");
                    speechAudioStream = new AudioInputStream(
                            new SequenceInputStream(speechAudioStream, audioInputStream),
                            audioInputStream.getFormat(),
                            audioInputStream.getFrameLength());
                }

//                new File("SPEECH.wav").delete();
                new File("sound.wav").delete();
                new File("only_speech.wav").delete();

                waveDataUtil.saveToFile("SPEECH", AudioFileFormat.Type.WAVE, speechAudioStream);
//                new MakeSound().playSound("only_speech.wav");
//                System.out.println(new File(file).delete());
//                files.add(file);
            }
            else {
                System.out.println(".");
                boolean delete = new File(file).delete();
//                System.out.println(delete);
            }
        }
        new MakeSound().playSound(merge(audioInputStreams, ".wav", AudioFileFormat.Type.WAVE));
        System.out.println("end");
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
}
