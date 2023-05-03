package com.example.speech.audio.detectors;

import ai.picovoice.porcupine.Porcupine;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@Service
public class TriggerWordDetector {

    @Value("${porcupine.access-key}")
    private String accessKey;

    @Value("${porcupine.library-path}")
    private String libPath;

    @Value("${porcupine.model-path}")
    private String modelPath;

    @Value("${porcupine.keyword-paths}")
    private String[] keywordPaths;

    @Value("${porcupine.sensitivities}")
    private float[] sensitivities;
    private Porcupine porcupine;

    public boolean isTriggerWordDetected(String audioFilePath){
        return isDetected(new File(audioFilePath));
    }

    private boolean isDetected(File inputAudioFile) {
        // create keywords from keyword_paths
        String[] keywords = new String[keywordPaths.length];
        for (int i = 0; i < keywordPaths.length; i++) {
            File keywordFile = new File(keywordPaths[i]);
            if (!keywordFile.exists()) {
                throw new IllegalArgumentException(String.format("Keyword file at '%s' " +
                        "does not exist", keywordPaths[i]));
            }
            keywords[i] = keywordFile.getName().split("_")[0];
        }

        AudioInputStream audioInputStream;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(inputAudioFile);
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Audio format not supported. Please provide " +
                    "an input file of .au, .aiff or .wav format");
            return false;
        } catch (IOException e) {
            System.err.println("Could not find input audio file at " + inputAudioFile);
            return false;
        }

        boolean isDetected = false;
        try {
            if (porcupine == null) {
                porcupine = new Porcupine.Builder()
                        .setAccessKey(accessKey)
                        .setLibraryPath(libPath)
                        .setModelPath(modelPath)
                        .setKeywordPaths(keywordPaths)
                        .setSensitivities(sensitivities)
                        .build();
            }

            AudioFormat audioFormat = audioInputStream.getFormat();

            if (audioFormat.getSampleRate() != 16000.0f ||
                    audioFormat.getSampleSizeInBits() != 16) {
                throw new IllegalArgumentException(
                        String.format("Invalid input audio file format. " +
                                        "Input file must be a %dkHz, 16-bit audio file.",
                                porcupine.getSampleRate()));
            }

            if (audioFormat.getChannels() > 1) {
                System.out.println("Picovoice processes single-channel audio, " +
                        "but a multi-channel file was provided. Processing leftmost channel only.");
            }

            int frameIndex = 0;
            long totalSamplesRead = 0;
            short[] porcupineFrame = new short[porcupine.getFrameLength()];

            ByteBuffer sampleBuffer = ByteBuffer.allocate(audioFormat.getFrameSize());
            sampleBuffer.order(ByteOrder.LITTLE_ENDIAN);


            while (audioInputStream.available() != 0) {
                totalSamplesRead++;

                int numBytesRead = audioInputStream.read(sampleBuffer.array());
                if (numBytesRead < 2) {
                    break;
                }

                porcupineFrame[frameIndex++] = sampleBuffer.getShort(0);

                if (frameIndex == porcupineFrame.length) {
                    int result = porcupine.process(porcupineFrame);
                    if (result >= 0) {
//                        System.out.printf("Detected '%s' at %.02f sec\n", keywords[result],
//                                totalSamplesRead / (float) porcupine.getSampleRate());
                        System.out.println("\"Алеся\" is detected");
                        isDetected = true;
                        break;
                    }

                    frameIndex = 0;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return isDetected;
    }

    @PreDestroy
    private void destroy() {
        if (porcupine != null){
            porcupine.delete();
        }
    }
}
