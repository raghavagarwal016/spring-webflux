package com.reactive.order.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reactive.order.client.ProductClient;
import com.reactive.order.client.UserClient;
import com.reactive.order.dto.OrderDto;
import com.reactive.order.dto.OrderRequestDto;
import com.reactive.order.dto.OrderResponseDto;
import com.reactive.order.dto.RequestContext;
import com.reactive.order.repository.OrderRepository;
import com.reactive.order.service.OrderFullfillmentService;
import com.reactive.order.util.ConverterUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

@Service
@Slf4j
public class OrderFullfillmentServiceImpl implements OrderFullfillmentService {

  @Autowired
  private ProductClient productClient;

  @Autowired
  private UserClient userClient;

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private Sinks.Many<OrderDto> sink;


  @Override
  public Mono<OrderResponseDto> processOrder(Mono<OrderRequestDto> orderRequestDtoMono) {
    return
        orderRequestDtoMono
        .map(RequestContext::new)
        .flatMap(requestContext -> {
         return productClient.getProductById(requestContext.getOrderRequestDto().getProductId())
              .doOnNext(productDto -> requestContext.setProductDto(productDto))
              .thenReturn(requestContext);
        })
        .flatMap(requestContext -> {
          return userClient.authorizeTransaction(ConverterUtil.transactionRequestDto(requestContext))
              .doOnNext(transactionResponseDto -> requestContext.setTransactionResponseDto(transactionResponseDto))
              .thenReturn(requestContext);
        })
        .map(requestContext -> ConverterUtil.toOrder(requestContext))
        .map(order -> orderRepository.save(order))
        .map(order -> ConverterUtil.toOrderResponseDto(order))
        .subscribeOn(Schedulers.boundedElastic());
  }

  @Override
  public Flux<OrderResponseDto> generateOrder() {
    return Flux
        .zip(userClient.getAllUsers(), productClient.getallProducts())
        .map(combinedFlux -> new OrderRequestDto(combinedFlux.getT1().getId(), combinedFlux.getT2().getId()))
        .map(RequestContext::new)
        .flatMap(requestContext -> {
          return productClient.getProductById(requestContext.getOrderRequestDto().getProductId())
              .doOnNext(productDto -> requestContext.setProductDto(productDto))
              .thenReturn(requestContext);
        })
        .flatMap(requestContext -> {
          return userClient.authorizeTransaction(ConverterUtil.transactionRequestDto(requestContext))
              .doOnNext(transactionResponseDto -> requestContext.setTransactionResponseDto(transactionResponseDto))
              .thenReturn(requestContext);
        })
        .map(requestContext -> ConverterUtil.toOrder(requestContext))
        .map(order -> orderRepository.save(order))
        .map(order -> ConverterUtil.toOrderResponseDto(order))
        .subscribeOn(Schedulers.boundedElastic());
  }

  @Override
  public Mono<OrderDto> saveOrder(Mono<OrderDto> orderDtoMono) {
    return orderDtoMono
        .map(orderDto -> ConverterUtil.toOrderFromOrderDto(orderDto))
        .map(order -> orderRepository.save(order))
        .map(order -> ConverterUtil.toOrderDtoFromOrder(order))
        .doOnNext(sink::tryEmitNext)
        .subscribeOn(Schedulers.boundedElastic());
  }


}
