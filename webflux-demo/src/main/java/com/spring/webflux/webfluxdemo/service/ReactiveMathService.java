package com.spring.webflux.webfluxdemo.service;

import com.spring.webflux.webfluxdemo.dto.MultiplyRequest;
import com.spring.webflux.webfluxdemo.dto.Response;
import com.spring.webflux.webfluxdemo.dto.ResponseDouble;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveMathService {

  Mono<Response> findSquare(int input);

  Flux<Response> multiplicationTable(int input);

  Mono<Response> mutilply(Mono<MultiplyRequest> requestMono);

  Mono<ResponseDouble> add(int first, int second);

  Mono<ResponseDouble> substract(int first, int second);

  Mono<ResponseDouble> mutiply(int first, int second);

  Mono<ResponseDouble> divide(int first, int second);

}
