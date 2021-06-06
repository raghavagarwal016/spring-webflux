package com.reactive.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reactive.order.dto.OrderDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Configuration
public class SinkConfig {

  @Bean
  public Sinks.Many<OrderDto> sink() {
    return Sinks.many().replay().limit(1);
  }

  @Bean
  public Flux<OrderDto> orderBroadcast(Sinks.Many<OrderDto> sink) {
    return sink.asFlux();
  }

}
