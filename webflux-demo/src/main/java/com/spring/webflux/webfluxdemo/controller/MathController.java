package com.spring.webflux.webfluxdemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.webflux.webfluxdemo.dto.MultiplyRequest;
import com.spring.webflux.webfluxdemo.dto.Response;
import com.spring.webflux.webfluxdemo.exception.InputValidationException;
import com.spring.webflux.webfluxdemo.service.MathService;
import com.spring.webflux.webfluxdemo.service.ReactiveMathService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/math")
public class MathController {

  @Autowired
  private MathService mathService;

  @Autowired
  private ReactiveMathService reactiveMathService;

  //traditional rest api
  @GetMapping("/square/{input}")
  public ResponseEntity<Response> findSquare(@PathVariable("input") int input) {
    Response response = mathService.findSquare(input);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  //traditional rest api
  @GetMapping("/muliptication-table/{input}")
  public ResponseEntity<List<Response>> findMultiplicationTable(@PathVariable("input") int input) {
    List<Response> responses = mathService.multiplicationTable(input);
    return new ResponseEntity<>(responses, HttpStatus.OK);
  }

  // reactive api
  @GetMapping(value = "/reactive/square/{input}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public ResponseEntity<Mono<Response>> findSquareReactive(@PathVariable("input") int input) {
    if (input < 10 || input > 20) {
      throw new InputValidationException(input);
    }
    Mono<Response> response = reactiveMathService.findSquare(input);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  // reactive api Mono.error()
  @GetMapping(value = "/reactive/square/{input}/throws", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public ResponseEntity<Mono<Response>> findSquareReactiveMonoError(@PathVariable("input") int input) {
    Mono<Response> response = Mono.just(input).handle((integer, synchronousSink) -> {
      if (integer < 10 || integer > 20) {
        synchronousSink.error(new InputValidationException(integer));
      } else {
        synchronousSink.next(integer);
      }
    }).cast(Integer.class).flatMap(integer -> reactiveMathService.findSquare(integer));
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  // reactive api validation
  @GetMapping(value = "/reactive/square/{input}/validate", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Mono<ResponseEntity> findSquareReactiveValidate(@PathVariable("input") int input) {
    Mono<ResponseEntity> response =
        Mono.just(input).filter(integer -> integer >= 10 && integer <= 20).cast(Integer.class)
            .flatMap(integer -> reactiveMathService.findSquare(integer))
            .map(response1 -> new ResponseEntity(response1, HttpStatus.OK))
            .defaultIfEmpty(new ResponseEntity(HttpStatus.BAD_REQUEST));
    return response;
  }

  //reactive get with streming response api
  @GetMapping(value = "/reactive/muliptication-table/{input}", produces = MediaType.TEXT_EVENT_STREAM_VALUE )
  public ResponseEntity<Flux<Response>> findMultiplicationTableReactive(@PathVariable("input") int input) {
    Flux<Response> responses = reactiveMathService.multiplicationTable(input);
    return new ResponseEntity<>(responses, HttpStatus.OK);
  }

  //reactive get with params
  @GetMapping(value = "/reactive/param/square")
  public ResponseEntity<Mono<Response>> squareParam(@RequestParam("input") int input) {
    Mono<Response> response = reactiveMathService.findSquare(input);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  //reactive post api
  @PostMapping(value = "/reactive/multiply")
  public ResponseEntity<Mono<Response>> multiply(@RequestBody Mono<MultiplyRequest> requestMono) {
    Mono<Response> response = reactiveMathService.mutilply(requestMono);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

}


