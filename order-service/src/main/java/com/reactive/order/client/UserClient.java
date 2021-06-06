package com.reactive.order.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.reactive.order.dto.TransactionRequestDto;
import com.reactive.order.dto.TransactionResponseDto;
import com.reactive.order.dto.UserDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserClient {

  private final WebClient userWebClient;
  private final WebClient userTransactionWebClient;

  public UserClient(@Value("${user.service.url}") String userUrl,
      @Value("${user.transaction.service.url}") String userTransactionUrl) {
    userWebClient = WebClient.builder().baseUrl(userUrl).build();
    userTransactionWebClient = WebClient.builder().baseUrl(userTransactionUrl).build();
  }

  public Mono<TransactionResponseDto> authorizeTransaction(TransactionRequestDto transactionRequestDto) {
    return userTransactionWebClient.post().uri("/create").bodyValue(transactionRequestDto).retrieve()
        .bodyToMono(TransactionResponseDto.class);
  }

  public Flux<UserDto> getAllUsers() {
    return userWebClient.get().uri("/all").retrieve().bodyToFlux(UserDto.class);
  }
}
