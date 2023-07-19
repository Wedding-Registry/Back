package com.wedding.serviceapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prod/hello")
@Slf4j
public class ProdController {

    @GetMapping
    public String hello() {
        log.info("[hello controller]");
        return "hello";
    }
}
