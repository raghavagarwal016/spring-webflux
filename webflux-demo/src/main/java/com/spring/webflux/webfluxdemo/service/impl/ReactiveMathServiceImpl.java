package com.spring.webflux.webfluxdemo.service.impl;

import java.time.Duration;

import org.springframework.stereotype.Service;

import com.spring.webflux.webfluxdemo.dto.MultiplyRequest;
import com.spring.webflux.webfluxdemo.dto.Response;
import com.spring.webflux.webfluxdemo.dto.ResponseDouble;
import com.spring.webflux.webfluxdemo.service.ReactiveMathService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ReactiveMathServiceImpl implements ReactiveMathService {

  @Override
  public Mono<Response> findSquare(int input) {
    return Mono
        .fromSupplier(() -> new Response(input * input));
  }

  @Override
  public Flux<Response> multiplicationTable(int input) {
    return Flux
        .range(1, 10)
        .delayElements(Duration.ofSeconds(1))
        .doOnNext(i -> log.info("math-reactive-service-multiplication: " + i))
        .map(integer -> new Response(integer * input));
  }

  @Override
  public Mono<Response> mutilply(Mono<MultiplyRequest> requestMono) {
    return requestMono
        .map(mutliplyRequest -> new Response(mutliplyRequest.getFirst() * mutliplyRequest.getSecond()));
  }

  @Override
  public Mono<ResponseDouble> add(int first, int second) {
    return Mono.fromSupplier(() -> new ResponseDouble(first + second));
  }

  @Override
  public Mono<ResponseDouble> substract(int first, int second) {
    return Mono.fromSupplier(() -> new ResponseDouble(first - second));
  }

  @Override
  public Mono<ResponseDouble> mutiply(int first, int second) {
    return Mono.fromSupplier(() -> new ResponseDouble(first * second));
  }

  @Override
  public Mono<ResponseDouble> divide(int first, int second) {
    return Mono.fromSupplier(() -> new ResponseDouble(Double.valueOf(first) / second));
  }

}
