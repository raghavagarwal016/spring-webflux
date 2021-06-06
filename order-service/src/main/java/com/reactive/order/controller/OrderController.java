package com.reactive.order.controller;

import javax.print.attribute.standard.Media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reactive.order.client.ProductClient;
import com.reactive.order.dto.OrderDto;
import com.reactive.order.dto.OrderRequestDto;
import com.reactive.order.dto.OrderResponseDto;
import com.reactive.order.service.OrderFullfillmentService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/order")
public class OrderController {

  @Autowired
  private OrderFullfillmentService orderFullfillmentService;

  @Autowired
  private Flux<OrderDto> orderBroadcast;

  @PostMapping("/purchase")
  public Mono<ResponseEntity<OrderResponseDto>> purchaseOrder(@RequestBody Mono<OrderRequestDto> orderRequestDtoMono) {
    return
        orderFullfillmentService.processOrder(orderRequestDtoMono)
        .map(orderResponseDto -> new ResponseEntity<>(orderResponseDto, HttpStatus.OK))
        .defaultIfEmpty(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
  }

  @PostMapping("/save")
  public Mono<ResponseEntity<OrderDto>> saveOrder(@RequestBody Mono<OrderDto> orderDtoMono) {
    return
        orderFullfillmentService.saveOrder(orderDtoMono)
            .map(orderResponseDto -> new ResponseEntity<>(orderResponseDto, HttpStatus.OK))
            .defaultIfEmpty(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
  }

  @GetMapping(value = "/generate", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public ResponseEntity<Flux<OrderResponseDto>> generateOrders() {
    return new ResponseEntity<>(orderFullfillmentService.generateOrder(), HttpStatus.OK);
  }

  @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public ResponseEntity<Flux<OrderDto>> getOrderStream() {
    return new ResponseEntity<>(orderBroadcast, HttpStatus.OK);
  }


}
