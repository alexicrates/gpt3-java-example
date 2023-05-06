package com.example.speech.audio.streamer;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
@Setter
public class AudioStreamerRunnable implements Runnable {
    AtomicBoolean isTurned = new AtomicBoolean(true);
    private final TargetDataLine line;
    private final Queue<byte[]> bufferQueue;
    private final AudioFormat format;
    private final int bufferLengthInFrames;
    private final int frameSizeInBytes;
    private final int bufferLengthInBytes;
    private ByteArrayOutputStream out;


    public AudioStreamerRunnable() {
        bufferQueue = new LinkedBlockingQueue<>();
        format = new AudioFormat(ApplicationProperties.ENCODING, ApplicationProperties.RATE, ApplicationProperties.SAMPLE_SIZE, ApplicationProperties.CHANNELS, (ApplicationProperties.SAMPLE_SIZE / 8) * ApplicationProperties.CHANNELS, ApplicationProperties.RATE, ApplicationProperties.BIG_ENDIAN);
        line = getTargetDataLineForRecord();
        out = new ByteArrayOutputStream();

        frameSizeInBytes = format.getFrameSize();
        bufferLengthInFrames = line.getBufferSize() / 8;
        bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
    }

    @SneakyThrows
    public TargetDataLine getTargetDataLineForRecord() {
       TargetDataLine line;
       DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
           if (!AudioSystem.isLineSupported(info)) {
               throw new Exception("CAN'T DETECT LINE");
               //line = (TargetDataLine) AudioSystem.getLine(info);
               //line.open(format, line.getBufferSize());
           }
           try {
               line = (TargetDataLine) AudioSystem.getLine(info);
               line.open(format, line.getBufferSize());
           } catch (final Exception ex) {
//               return null;
                throw ex;
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
    }

    public void setTurned(boolean isTurned){
        this.isTurned.set(isTurned);
    }

    public void buildByteOutputStream(final ByteArrayOutputStream out, TargetDataLine line, int frameSizeInBytes, final int bufferLengthInBytes) throws IOException {
        final byte[] data = new byte[bufferLengthInBytes];
        int numBytesRead;

        while (!Thread.currentThread().isInterrupted()) {
            if (isTurned.get()){
                line.start();
                if ((numBytesRead = line.read(data, 0, bufferLengthInBytes)) == -1) {
                    break;
                }
                out.write(data, 0, numBytesRead);
            }
            else {
                if (line.available() > 0)
                    line.flush();
            }
        }
    }

    public AudioInputStream convertToAudioIStream(final ByteArrayOutputStream out, int frameSizeInBytes) {
        byte[] audioBytes = out.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
        AudioInputStream audioStream = new AudioInputStream(bais, format, audioBytes.length / frameSizeInBytes);
        return audioStream;
    }

    public AudioInputStream getNewAudioInputStream(){
        AudioInputStream audioInputStream = convertToAudioIStream(out, frameSizeInBytes);
        out.reset();
        return audioInputStream;
    }
}
