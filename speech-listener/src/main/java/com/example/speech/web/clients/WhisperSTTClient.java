package com.example.speech.web.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;

import java.io.File;

@FeignClient(name = "stt", url = "localhost:5000", configuration = FeignConfig.class)
public interface WhisperSTTClient {
    @PostMapping(value = "/whisper", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    byte[] postAudioFile(@RequestPart(value = "file") File file);
}
