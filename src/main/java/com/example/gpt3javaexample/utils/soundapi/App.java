package com.example.gpt3javaexample.utils.soundapi;

import javax.sound.sampled.AudioFormat;

public class App {
    public static void main(String[] args) throws Exception {
//
//        AudioFormat format = buildAudioFormatInstance();
//        WaveDataUtil wd = new WaveDataUtil();
//        SoundRecorder soundRecorder = new SoundRecorder();
//        soundRecorder.build(format);
//
//        System.out.println("Start recording ....");
//        soundRecorder.start();
//        Thread.sleep(3000);
//        soundRecorder.stop();
//
//        Thread.sleep(1000);
//        File file = wd.saveToFile("sound", AudioFileFormat.Type.WAVE, soundRecorder.getAudioInputStream());

////        Process exec = Runtime.getRuntime().exec("python3 ./python_scripts/speech_detection.py -f ".concat(fileName));
//        Process exec = Runtime.getRuntime().exec(new String[]{"python3", "/home/alexicrates/Downloads/Telegram Desktop/gpt3-java-example-new/gpt3-java-example/python_scripts/speech_detection.py", "-f",
//                file.getName()});
//        exec.errorReader().lines().forEach(System.out::println);
//        exec.inputReader().lines().forEach(System.out::println);
//
//        int i = exec.waitFor();
//
//        System.out.println(i);
//        exec.errorReader().lines().forEach(System.out::println);
//        exec.inputReader().lines().forEach(System.out::println);
//
//        file.delete();
//
//        Process exec = Runtime.getRuntime().exec(new String[]{"whisper", "test.wav", "--language", "Russian", "--model", "base"});
//        exec1.inputReader().lines().forEach(System.out::println);

//        Process exec = Runtime.getRuntime().exec("python3 ./python_scripts/speech_detection.py -f ".concat("test.wav"));
//        int i = exec.waitFor();
//
//        System.out.println(i);
//        exec.errorReader().lines().forEach(System.out::println);
//        exec.inputReader().lines().forEach(System.out::println);
    }

    public static AudioFormat buildAudioFormatInstance() {
        ApplicationProperties aConstants = new ApplicationProperties();
        AudioFormat.Encoding encoding = aConstants.ENCODING;
        float rate = aConstants.RATE;
        int channels = aConstants.CHANNELS;
        int sampleSize = aConstants.SAMPLE_SIZE;
        boolean bigEndian = aConstants.BIG_ENDIAN;

        return new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);
    }
}