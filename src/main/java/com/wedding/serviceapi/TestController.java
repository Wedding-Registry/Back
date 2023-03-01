package com.wedding.serviceapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @GetMapping("/")
    public String webHookTest() {
        log.info("nohup log test");
        return "jenkins ci/cd and remove none image test";
    }

    @GetMapping("/test")
    public String webHookTest2() {
        log.info("webhooktest");
        return "jenkins test2";
    }

    @GetMapping("/test2")
    public String webHookTest3() {
        log.info("webhookTest2");
        return "jenkins feature push not ci build test";
    }
}
