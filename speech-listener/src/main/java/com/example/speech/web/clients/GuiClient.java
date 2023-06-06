package com.example.speech.web.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient(value = "gui", url = "${GUI_HOST}")
public interface GuiClient {
    @RequestMapping(method = GET, path = "/record/start")
    String startRecord();

    @RequestMapping(method = GET, path = "/record/end")
    String endRecord();

    @RequestMapping(method = GET, value = "/message/user", params = {"text"})
    String appendUserMessage(@RequestParam("text") String text);

    @RequestMapping(method = GET, value = "/message/bot", params = {"text"})
    String appendBotMessage(@RequestParam("text") String text);

    @GetMapping(value = "/error/{message}")
    void showErrorMessage(@PathVariable("message") String message);
}
