package com.spring.webflux.webfluxdemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;

import com.spring.webflux.webfluxdemo.dto.FailedValidationResponse;
import com.spring.webflux.webfluxdemo.dto.MultiplyRequest;
import com.spring.webflux.webfluxdemo.dto.Response;
import com.spring.webflux.webfluxdemo.dto.ResponseDouble;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class GetSingleResponseTest extends WebfluxDemoApplicationTests{

  @Autowired
  private WebClient webClient;

  @Test
  public void blockTest() {
    Response response = webClient
        .get()
        .uri("/route/square/{input}", 5)
        .retrieve()
        .bodyToMono(Response.class)
        .block();
    System.out.println(response);
  }

  @Test
  public void stepVerifierTest() {
    Mono<Response> response = webClient
        .get()
        .uri("/route/square/{input}", 5)
        .retrieve()
        .bodyToMono(Response.class);
    StepVerifier
        .create(response)
        .expectNextMatches(res -> res.getOutput() == 25)
        .verifyComplete();

  }

  @Test
  public void fluxResponseTest() {
    Flux<Response> response = webClient
        .get()
        .uri("/route/muliptication-table/{input}", 5)
        .retrieve()
        .bodyToFlux(Response.class);

    StepVerifier
        .create(response)
        .expectNextCount(10)
        .verifyComplete();
  }

  @Test
  public void fluxStreamResponseTest() {
    Flux<Response> response = webClient
        .get()
        .uri("/route/streaming/muliptication-table/{input}", 5)
        .retrieve()
        .bodyToFlux(Response.class)
        .doOnNext(System.out::println);

    StepVerifier
        .create(response)
        .expectNextCount(10)
        .verifyComplete();
  }


  @Test
  public void postRequestTest()  {
    Mono<Response> response = webClient
        .post()
        .uri("/route/multiply")
        .bodyValue(new MultiplyRequest(10, 5))
        .retrieve()
        .bodyToMono(Response.class);

    StepVerifier
        .create(response)
        .expectNextMatches(res -> res.getOutput() == 50)
        .verifyComplete();
  }

  @Test
  public void calculateRequestTest()  {
    Mono<ResponseDouble> response = webClient
        .get()
        .uri("/calculate/{first}/{second}", 5, 10)
        .headers(httpHeader -> httpHeader.set("op", "+"))
        .retrieve()
        .bodyToMono(ResponseDouble.class);

    StepVerifier
        .create(response)
        .expectNextMatches(res -> res.getOutput() == 15.0)
        .verifyComplete();
  }

  @Test
  public void badRequestTest() {
    Mono<Object> response = webClient
        .get()
        .uri("/calculate/{first}/{second}", 5, 10)
        .headers(httpHeader -> httpHeader.set("op", "="))
        .exchangeToMono(clientResponse -> {
          if(clientResponse.rawStatusCode() == 400) {
            return clientResponse.bodyToMono(FailedValidationResponse.class);
          }
          else {
            return clientResponse.bodyToMono(ResponseDouble.class);
          }
        });

    StepVerifier
        .create(response)
        .expectNextCount(1)
        .verifyComplete();
  }

  @Test
  public void paramsRequestTest() {
    Mono<Response> response = webClient.get()
        .uri(uriBuilder -> uriBuilder.path("/math/reactive/param/square").queryParam("input", "10").build())
        .retrieve()
        .bodyToMono(Response.class)
        .doOnNext(System.out::println);

    StepVerifier
        .create(response)
        .expectNextCount(1)
        .verifyComplete();
  }

  @Test
  public void calculateRequestTest2() {
    Flux<String> response =
        Flux
            .range(1, 5)
            .flatMap(i ->
                Flux
                    .just("+", "-", "*", "/")
                    .flatMap(operation -> {
                        return webClient.get()
                            .uri("/calculate/{first}/{second}", 10, i)
                            .headers(httpHeader -> httpHeader.set("op", operation))
                            .retrieve()
                            .bodyToMono(ResponseDouble.class)
                            .map(j -> "10" + operation + i + "=" + j.getOutput());
    }))
            .doOnNext(System.out::println);
    StepVerifier.create(response).expectNextCount(20).verifyComplete();
  }


}
