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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioFileFormat;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.example.speech.audio.streamer.AudioFilesUtils.mergeFiles;

@Service
public class WorkflowService {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Value("${tts.ttl:5}")
    private int ttsTtl;

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
            String gptResponse;
            String sileroResponse;

            ArrayList<File> speechSamples = speechListenerRecorder.getTempSpeechAudioFiles();
            String mergeFilePath = mergeFiles(speechSamples, ".wav", AudioFileFormat.Type.WAVE);

            if(mergeFilePath == null){
                return;
            }

            //post audio to whisper
            byte[] responseBytes;
            try {
                responseBytes = sttClient.postAudioFile(new File(mergeFilePath));
            } catch (Exception e) {
                reportErrorMessage(Services.STT);
                throw new RuntimeException(e);
            }

            //read transcript
            try {
                WhisperResponse whisperResponse = OBJECT_MAPPER.readValue(responseBytes, WhisperResponse.class);
                whisperTranscript = whisperResponse.getResults().get(0).getTranscript();

                System.out.println(new Date(System.currentTimeMillis()) + " - Transcript: " + whisperTranscript);

                appendMessageToGui(whisperTranscript, Services.STT);
            } catch (IOException e) {
                System.out.println("CAN'T PARSE WHISPER JSON");
                e.printStackTrace();
            }

            //send transcript to gpt
            try {
		System.out.println(new Date(System.currentTimeMillis()) + " - Sending request to GPT model");
                gptResponse = gptApiClient.sendRequest(whisperTranscript, true);
                System.out.println(new Date(System.currentTimeMillis()) + " - GPT Response: " + gptResponse);

                appendMessageToGui(gptResponse, Services.GPT);
            } catch (Exception e) {
                reportErrorMessage(Services.GPT);
                throw new RuntimeException(e);
            }

            // send gpt response to silerotts
            try {
                sileroResponse = ttsClient.sendText(gptResponse);
                System.out.println(new Date(System.currentTimeMillis()) + " - Silero response: " + sileroResponse);
            } catch (Exception e) {
                reportErrorMessage(Services.TTS);
                throw new RuntimeException(e);
            }

            waitUntilAudioPlayingIsFinished();

        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            cleanup();
        }
    }

    private void waitUntilAudioPlayingIsFinished(){
        System.out.println(new Date(System.currentTimeMillis()) + " - Waiting until audio playing is finished...");
        boolean isPlaying = true;
        while (isPlaying){
            try {
                isPlaying = ttsClient.status().contains("true");
                if (isPlaying) {
                    Thread.sleep(1000);
                }
            }
            catch (InterruptedException e){
                System.out.println(e.getMessage());
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                reportErrorMessage(Services.TTS);
                return;
            }
        }
        System.out.println(new Date(System.currentTimeMillis()) + " - Audio playing is finished...");
    }

    private void reportErrorMessage(Services service){
        guiClient.showErrorMessage(service.errorMessage());
    }

    private void appendMessageToGui(String message, Services service){
        try {
            if (service.equals(Services.STT)){
                guiClient.appendUserMessage(message);
            }
            else if (service.equals(Services.GPT)){
                guiClient.appendBotMessage(message);
            }
        } catch (Exception e) {
            System.out.println("No GUI is present");
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
