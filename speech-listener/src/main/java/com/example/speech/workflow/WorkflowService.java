package com.example.speech.workflow;

import com.example.speech.audio.detectors.SpeechDetector;
import com.example.speech.audio.listener.SpeechListenerRecorder;
import com.example.speech.web.clients.GptApiClient;
import com.example.speech.web.clients.GuiClient;
import com.example.speech.web.clients.SileroTTSClient;
import com.example.speech.web.clients.WhisperSTTClient;
import com.example.speech.web.dto.WhisperResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioFileFormat;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.speech.audio.streamer.AudioFilesUtils.mergeFiles;

@Service
public class WorkflowService {
    private static final String SPEECH_FILE = "only_speech.wav";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final SpeechListenerRecorder speechListenerRecorder;
    private final SpeechDetector speechDetector;
    private final WhisperSTTClient sttClient;
    private final GptApiClient gptApiClient;
    private final SileroTTSClient ttsClient;
    private final GuiClient guiClient;

    @Autowired
    public WorkflowService(SpeechListenerRecorder speechListenerRecorder,
                           SpeechDetector speechDetector,
                           WhisperSTTClient sttClient,
                           GptApiClient gptApiClient,
                           SileroTTSClient ttsClient,
                           GuiClient guiClient) {
        this.speechListenerRecorder = speechListenerRecorder;
        this.speechDetector = speechDetector;
        this.sttClient = sttClient;
        this.gptApiClient = gptApiClient;
        this.ttsClient = ttsClient;
        this.guiClient = guiClient;
    }

    @Scheduled(fixedDelay = 1000)
    public void listen() {
        try {
            String whisperTranscript = null;
            String gptResponse = null;
            String sileroResponse = null;

            ArrayList<File> speechSamples = speechListenerRecorder.getTempSpeechAudioFiles(2, 50);

            String mergeFilePath = mergeFiles(speechSamples, ".wav", AudioFileFormat.Type.WAVE);

            if(mergeFilePath == null){
                return;
            }

            speechDetector.isSpeech(mergeFilePath, true);

            byte[] responseBytes = sttClient.postAudioFile(new File(SPEECH_FILE));

            try {
                WhisperResponse whisperResponse = OBJECT_MAPPER.readValue(responseBytes, WhisperResponse.class);
                whisperTranscript = whisperResponse.getResults().get(0).getTranscript();
                System.out.println("Transcript: " + whisperTranscript);
            } catch (IOException e) {
                System.out.println("CAN'T PARSE JSON");
                e.printStackTrace();
            }

            if (whisperTranscript != null) {
                try {
                    guiClient.appendUserMessage(whisperTranscript);
                } catch (Exception e) {
                    System.out.println("NO GUI PROVIDED");
                }
                gptResponse = gptApiClient.sendRequest(whisperTranscript, true);
                System.out.println("GPT Response: " + gptResponse);
            }
            if (gptResponse != null) {
                try {
                    guiClient.appendBotMessage(gptResponse);
                } catch (Exception e) {
                    System.out.println("NO GUI PROVIDED");
                }
                sileroResponse = ttsClient.sendText(gptResponse);
                System.out.println("Silero response: " + sileroResponse);
            }

        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            cleanup();
        }
    }

    @PreDestroy
    private void destroy(){
        cleanup();
    }

    private void cleanup(){
        File currentDir = new File(".");
        for (File file : currentDir.listFiles()) {
            if (file.getName().contains(".wav")){
                file.delete();
            }
        }
    }

}
