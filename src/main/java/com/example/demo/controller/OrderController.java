package com.example.demo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.example.demo.dto.Order;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/Orders")
public class OrderController {

    @Autowired
    private ObjectMapper objectMapper;

    @Value(value = "${ordersUri}")
    private String ordersUri;

    @GetMapping("")
    public Flux<Order> findAll() {
        Flux<Order> flux = WebClient.create(ordersUri + "/api/Orders?api-version=1.0").get().retrieve()
                .bodyToFlux(Order.class).map(order -> {
                    return order;
                });
        return flux;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public Mono<Order> create(@RequestBody Order order) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        order.setOrderDate(formatter.format(LocalDateTime.now()));
        Mono<Order> mono = WebClient.create(ordersUri + "/api/Orders?api-version=1.0").post()
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).bodyValue(order).retrieve()
                .bodyToMono(Order.class);
        return mono;
    }

    @PutMapping("/{orderSn}")
    public Mono<Order> update(@PathVariable("orderSn") String orderSn, @RequestBody Order order) {
        Mono<Order> mono = WebClient.builder().baseUrl(ordersUri).build().put()
                .uri(uriBuilder -> uriBuilder.path("/api/Orders/{orderSn}").queryParam("api-version", "1.0")
                        .build(orderSn))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).bodyValue(order).retrieve()
                .bodyToMono(Order.class);
        return mono;
    }

    @DeleteMapping("/{orderSn}")
    public Mono<Void> delete(@PathVariable("orderSn") String orderSn) {
        log.info("orderSn={}", orderSn);
        return WebClient
                .builder().baseUrl(ordersUri).build().delete().uri(uriBuilder -> uriBuilder
                        .path("/api/Orders/{orderSn}").queryParam("api-version", "1.0").build(orderSn))
                .accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(Void.class);
    }

}
