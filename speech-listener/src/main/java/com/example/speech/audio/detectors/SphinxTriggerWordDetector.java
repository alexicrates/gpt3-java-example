package com.example.speech.audio.detectors;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Getter
@Setter
public class SphinxTriggerWordDetector {

    @Value("${sphinx.trigger-word-path}")
    private String triggerWordConfigPath;

    private LiveSpeechRecognizer recognizer;

    private boolean isListening = true;
    private Configuration configuration;

    @PostConstruct
    public void config() throws IOException {
        // Load configuration
         configuration = new Configuration();

        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath(triggerWordConfigPath + "alesya.dict");
        configuration.setLanguageModelPath(triggerWordConfigPath + "alesya.lm");
        configuration.setGrammarPath(triggerWordConfigPath);
        configuration.setGrammarName("alesya");
        configuration.setUseGrammar(true);

        recognizer = new LiveSpeechRecognizer(configuration);
    }

    @SneakyThrows
    public boolean waitForTriggerWord(){
        recognizer = new LiveSpeechRecognizer(configuration);

        Thread.sleep(1000);
        System.out.println("Listening...");
        recognizer.startRecognition(true);

        while (isListening) {
            String hypothesis = recognizer.getResult().getHypothesis();
            if (hypothesis.equals("alesya") && isListening) {
                System.out.println("Trigger word detected: " + hypothesis);
                recognizer.stopRecognition();
                return true;
            }
        }

        return false;
    }
}
