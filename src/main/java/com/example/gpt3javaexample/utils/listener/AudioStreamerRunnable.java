package com.example.gpt3javaexample.utils.listener;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.example.gpt3javaexample.utils.soundapi.ApplicationProperties.*;

public class AudioStreamerRunnable implements Runnable {
    private final TargetDataLine line;
    private final Queue<byte[]> bufferQueue;
    private final AudioFormat format;
    private final int bufferLengthInFrames;
    private final int frameSizeInBytes;
    private final int bufferLengthInBytes;
    private ByteArrayOutputStream out;

    public AudioFormat getFormat() {
         return format;
    }

    public int getBlock_size() {
        return bufferLengthInFrames;
    }

    public AudioStreamerRunnable() {
        bufferQueue = new LinkedBlockingQueue<>();
        format = new AudioFormat(ENCODING, RATE, SAMPLE_SIZE, CHANNELS, (SAMPLE_SIZE / 8) * CHANNELS, RATE, BIG_ENDIAN);
        line = getTargetDataLineForRecord();
        out = new ByteArrayOutputStream();

        frameSizeInBytes = format.getFrameSize();
        bufferLengthInFrames = line.getBufferSize() / 8;
        bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
    }

    public TargetDataLine getTargetDataLineForRecord() {
       TargetDataLine line;
       DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
           if (!AudioSystem.isLineSupported(info)) {
               return null;
           }
           try {
               line = (TargetDataLine) AudioSystem.getLine(info);
               line.open(format, line.getBufferSize());
           } catch (final Exception ex) {
               return null;
           }
           return line;
       }

    @Override
    public void run() {
        try {
            buildByteOutputStream(out, line, frameSizeInBytes, bufferLengthInBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        line.stop();
//        line.close();
    }

    public void buildByteOutputStream(final ByteArrayOutputStream out, final TargetDataLine line, int frameSizeInBytes, final int bufferLengthInBytes) throws IOException {
        final byte[] data = new byte[bufferLengthInBytes];
        int numBytesRead;

        line.start();
        while (!Thread.currentThread().isInterrupted()) {
            if ((numBytesRead = line.read(data, 0, bufferLengthInBytes)) == -1) {
                break;
            }
            out.write(data, 0, numBytesRead);
        }
    }

    public AudioInputStream convertToAudioIStream(final ByteArrayOutputStream out, int frameSizeInBytes) {
        byte audioBytes[] = out.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
        AudioInputStream audioStream = new AudioInputStream(bais, format, audioBytes.length / frameSizeInBytes);
        return audioStream;
    }

    public AudioInputStream getAudioInputStream(){
        AudioInputStream audioInputStream = convertToAudioIStream(out, frameSizeInBytes);
        out.reset();
        return audioInputStream;
    }

    public byte[] readFromQueue(){
            return bufferQueue.poll();
        }
}