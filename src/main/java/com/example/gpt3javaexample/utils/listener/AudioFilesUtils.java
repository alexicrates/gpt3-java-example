package com.example.gpt3javaexample.utils.listener;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.util.List;
import java.util.UUID;

public class AudioFilesUtils {
    public static String mergeFiles(List<File> audioFiles, String fileType, AudioFileFormat.Type audioType) throws IOException {
        if (audioFiles.isEmpty()){
            return null;
        }

        String mergeFile;
        List<AudioInputStream> audioInputStreams = audioFiles.stream().map(AudioFilesUtils::fileToAudioStream).toList();

        if (audioInputStreams.size() == 1){
            mergeFile = UUID.randomUUID() + ".wav";
            AudioSystem.write(audioInputStreams.get(0), AudioFileFormat.Type.WAVE, new File(mergeFile));
        }
        else {
            mergeFile = merge(audioInputStreams, fileType, audioType);
        }

        cleanup(audioFiles);
        return mergeFile;
    }

    private static String merge(List<AudioInputStream> audioInputStreams, String fileType, AudioFileFormat.Type audioType) {
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

    public static AudioInputStream fileToAudioStream(File file) {
        try {
            return AudioSystem.getAudioInputStream(file);
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void cleanup(List<File> files){
        for (File file : files) {
            file.delete();
        }
        files.clear();
    }
}
