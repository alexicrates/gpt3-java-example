package com.example.speech.audio;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Getter
public class SphinxTriggerWordDetector {

    @Value("${sphinx.trigger-word-path}")
    private String triggerWordConfigPath;

    private LiveSpeechRecognizer recognizer;

    @PostConstruct
    public void check() throws IOException {
        // Load configuration
        Configuration configuration = new Configuration();

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
        if (recognizer == null){
            System.out.println("SPEECH RECOGNIZER IS NOT CONFIGURED");
            return false;
        }
        Thread.sleep(1000);
        System.out.println("Listening...");
        recognizer.startRecognition(true);

        while (true) {
            String hypothesis = recognizer.getResult().getHypothesis();
            if (hypothesis.equals("alesya")) {
                System.out.println("Trigger word detected: " + hypothesis);
                recognizer.stopRecognition();
                return true;
            } else {
                System.out.println("Trigger word is not detected");
            }
        }
    }

}
