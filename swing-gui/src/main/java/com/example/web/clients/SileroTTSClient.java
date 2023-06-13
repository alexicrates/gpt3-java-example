package com.example.web.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "tts", url = "${TTS_HOST}")
public interface SileroTTSClient {
    @GetMapping(value = "/speaker/set", params = {"speaker"})
    String setSpeaker(@RequestParam("speaker") String speaker);
}
