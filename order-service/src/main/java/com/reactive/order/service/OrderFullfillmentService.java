package com.reactive.order.service;

import com.reactive.order.dto.OrderDto;
import com.reactive.order.dto.OrderRequestDto;
import com.reactive.order.dto.OrderResponseDto;
import com.reactive.order.entity.Order;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderFullfillmentService {

  Mono<OrderResponseDto> processOrder(Mono<OrderRequestDto> orderRequestDtoMono);

  Flux<OrderResponseDto> generateOrder();

  Mono<OrderDto> saveOrder(Mono<OrderDto> orderDtoMono);
}
