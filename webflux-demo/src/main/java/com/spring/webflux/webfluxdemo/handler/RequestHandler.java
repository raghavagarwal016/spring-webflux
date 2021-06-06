package com.spring.webflux.webfluxdemo.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.spring.webflux.webfluxdemo.dto.MultiplyRequest;
import com.spring.webflux.webfluxdemo.dto.Response;
import com.spring.webflux.webfluxdemo.dto.ResponseDouble;
import com.spring.webflux.webfluxdemo.exception.InputValidationException;
import com.spring.webflux.webfluxdemo.service.ReactiveMathService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class RequestHandler {

  @Autowired
  private ReactiveMathService reactiveMathService;

  public Mono<ServerResponse> squareHandler(ServerRequest serverRequest) {
    int input = Integer.parseInt(serverRequest.pathVariable("input"));
    Mono<Response> responseMono = this.reactiveMathService.findSquare(input);
    return ServerResponse.ok().body(responseMono, Response.class);
  }

  public Mono<ServerResponse> multiplicationHandler(ServerRequest serverRequest) {
    int input = Integer.parseInt(serverRequest.pathVariable("input"));
    Flux<Response> responseFlux = this.reactiveMathService.multiplicationTable(input);
    return ServerResponse.ok().body(responseFlux, Response.class);
  }

  public Mono<ServerResponse> multiplicationStreamingHandler(ServerRequest serverRequest) {
    int input = Integer.parseInt(serverRequest.pathVariable("input"));
    Flux<Response> responseFlux = this.reactiveMathService.multiplicationTable(input);
    return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(responseFlux, Response.class);
  }

  public Mono<ServerResponse> multiplyHandler(ServerRequest serverRequest) {
    Mono<MultiplyRequest> requestMono = serverRequest.bodyToMono(MultiplyRequest.class);
    Mono<Response> mono = this.reactiveMathService.mutilply(requestMono);
    return ServerResponse.ok().body(mono, Response.class);
  }

  public Mono<ServerResponse> squareHandlerWebValidation(ServerRequest serverRequest) {
    int input = Integer.parseInt(serverRequest.pathVariable("input"));
    if (input < 10 || input > 20) {
      return Mono.error(new InputValidationException(input));
    }
    Mono<Response> responseMono = this.reactiveMathService.findSquare(input);
    return ServerResponse.ok().body(responseMono, Response.class);
  }

  public Mono<ServerResponse> calculate(ServerRequest serverRequest) {
    int first = Integer.parseInt(serverRequest.pathVariable("first"));
    int second = Integer.parseInt(serverRequest.pathVariable("second"));
    Mono<ResponseDouble> responseMono;
    switch (serverRequest.headers().firstHeader("op")) {
      case "+":
        responseMono = reactiveMathService.add(first, second);
        break;
      case "-":
        responseMono = reactiveMathService.substract(first, second);
        break;
      case "*":
        responseMono = reactiveMathService.mutiply(first, second);
        break;
      case "/":
        responseMono = reactiveMathService.divide(first, second);
        break;
      default:
        return Mono.error(new RuntimeException("Check the header"));
    }
    return ServerResponse.ok().body(responseMono, ResponseDouble.class);
  }

}
