package com.example.web.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Component
@FeignClient(value = "localhost", url = "${LISTENER_HOST}")
public interface ListenerClient {
    @RequestMapping(method = GET, path = "/micro/on")
    String turnMicroOn();

    @RequestMapping(method = GET, path = "/micro/off")
    String turnMicroOff();

}
