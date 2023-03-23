//package com.example.gpt3javaexample;
//
//import javax.sound.sampled.*;
//import java.io.*;
//import java.util.List;
//
//import static com.example.gpt3javaexample.utils.listener.AudioFilesUtils.mergeFiles;
//import static com.example.gpt3javaexample.utils.soundapi.ApplicationProperties.*;
//import static com.example.gpt3javaexample.utils.soundapi.ApplicationProperties.BIG_ENDIAN;
//
//public class MergeAudio {
//    public final static String SOUND_PATH = "/home/alexicrates/Music/";
//    static List<String> fileNames = List.of("test.wav", "speech_orig.wav", "en_example.wav");
//
//    public static void main(String[] args) throws IOException {
//        List<File> files = fileNames.stream().map(fileName -> new File(SOUND_PATH.concat(fileName))).toList();
//
//        List<AudioInputStream> audioInputStreams = files.stream().map(file -> {
//            try {
//                return AudioSystem.getAudioInputStream(file);
//            } catch (UnsupportedAudioFileException | IOException e) {
//                throw new RuntimeException(e);
//            }
//        }).toList();
//
//        System.out.println("merging");
//        mergeFiles(audioInputStreams, ".wav", AudioFileFormat.Type.WAVE);
//        System.out.println("merged");
//    }
//
//    public static AudioInputStream convertToAudioIStream(ByteArrayInputStream in) {
//        AudioFormat format = new AudioFormat(ENCODING, RATE, SAMPLE_SIZE, CHANNELS, (SAMPLE_SIZE / 8) * CHANNELS, RATE, BIG_ENDIAN);
//        return new AudioInputStream(in, format, in.available() / format.getFrameSize());
//    }
//
//}
