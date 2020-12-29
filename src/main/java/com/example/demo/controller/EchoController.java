package com.example.demo.controller;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/echo")
public class EchoController {

    @GetMapping("/time")
    public Mono<String> echeTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        Mono<String> mono = Mono.just(formatter.format(ZonedDateTime.now()));
        return mono;
    }

    @GetMapping("/proxy")
    public Mono<String> echo(@RequestParam(name = "target", required = true) String target) {
        Mono<String> mono = WebClient.builder().baseUrl(target).build().get().retrieve().bodyToMono(String.class);
        return mono;
    }
}
