package com.reactive.user.service.impl;

import static com.reactive.user.dto.TransactionStatus.APPROVED;
import static com.reactive.user.dto.TransactionStatus.DECLINED;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reactive.user.dto.TransactionDto;
import com.reactive.user.dto.TransactionRequestDto;
import com.reactive.user.dto.TransactionResponseDto;
import com.reactive.user.dto.TransactionStatus;
import com.reactive.user.repository.UserRepository;
import com.reactive.user.repository.UserTransactionRepository;
import com.reactive.user.service.TransactionService;
import com.reactive.user.util.ConverterUtil;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserTransactionRepository userTransactionRepository;


  @Override
  public Mono<TransactionResponseDto> createTransaction(final TransactionRequestDto transactionRequestDto) {
    return userRepository
        .updateUserBalance(transactionRequestDto.getUserId(), transactionRequestDto.getAmount())
        .doOnNext(aBoolean -> System.out.println(transactionRequestDto))
        .filter(Boolean::booleanValue)
        .flatMap(aBoolean -> userTransactionRepository
            .save(ConverterUtil.toUserTransactionFromTransactionRequestDto(transactionRequestDto)))
        .map(userTransaction -> ConverterUtil.toUserTransactionResponse(transactionRequestDto, APPROVED))
        .defaultIfEmpty(ConverterUtil.toUserTransactionResponse(transactionRequestDto, DECLINED));
  }

  @Override
  public Flux<TransactionDto> getTransactionForUser(Integer id) {
    return userTransactionRepository.findByUserId(id)
        .map(userTransaction -> ConverterUtil.transactionDtoFromUserTransaction(userTransaction));
  }
}
