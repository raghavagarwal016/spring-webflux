package com.reactive.user.service;

import com.reactive.user.dto.TransactionDto;
import com.reactive.user.dto.TransactionRequestDto;
import com.reactive.user.dto.TransactionResponseDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionService {

  Mono<TransactionResponseDto> createTransaction(final TransactionRequestDto transactionRequestDtoMono);

  Flux<TransactionDto> getTransactionForUser(Integer id);

}
