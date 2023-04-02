package com.example.speech.web.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient(value = "localhost", url = "localhost:8080/")
public interface GptApiClient {
    @RequestMapping(method = GET, params = {"prompt", "new_chat"})
    String sendRequest(@RequestParam("prompt") String prompt,
                       @RequestParam("new_chat") Boolean new_chat);

}
