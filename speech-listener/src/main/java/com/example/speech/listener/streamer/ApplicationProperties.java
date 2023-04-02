package com.example.speech.listener.streamer;
import javax.sound.sampled.AudioFormat;

public class ApplicationProperties {
    static public final AudioFormat.Encoding ENCODING = AudioFormat.Encoding.PCM_SIGNED;
    static public final float RATE = 16000.0f;
    static public final int CHANNELS = 1;
    static public final int SAMPLE_SIZE = 16;
    static public final boolean BIG_ENDIAN = true;
}