package com.reactive.user.controller;

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

import com.reactive.user.dto.TransactionDto;
import com.reactive.user.dto.TransactionRequestDto;
import com.reactive.user.dto.TransactionResponseDto;
import com.reactive.user.service.TransactionService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/user/transaction")
public class UserTransactionController {

  @Autowired
  private TransactionService transactionService;

  @PostMapping("/create")
  public Mono<ResponseEntity<TransactionResponseDto>> createTransaction(
      @RequestBody Mono<TransactionRequestDto> transactionRequestDtoMono) {
    return transactionRequestDtoMono
        .flatMap(transactionRequestDto -> transactionService.createTransaction(transactionRequestDto))
        .map(transactionResponseDto -> new ResponseEntity<>(transactionResponseDto, HttpStatus.OK));
  }

  @GetMapping(value = "/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public ResponseEntity<Flux<TransactionDto>> getTransactionByUser(@PathVariable("userId") int userId) {
    return new ResponseEntity<>(transactionService.getTransactionForUser(userId), HttpStatus.OK);
  }
}
