package com.example.speech.web.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "tts", url = "localhost:5001")
public interface SileroTTSClient {
    @GetMapping(value = "/tts", params = {"text"})
    String sendText(@RequestParam("text") String text);
}
