package com.reactive.user.util;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.reactive.user.dto.TransactionDto;
import com.reactive.user.dto.TransactionRequestDto;
import com.reactive.user.dto.TransactionResponseDto;
import com.reactive.user.dto.TransactionStatus;
import com.reactive.user.dto.UserDto;
import com.reactive.user.entity.User;
import com.reactive.user.entity.UserTransaction;

public class ConverterUtil {

  public static UserDto toUserDtoFromUser(User user) {
    UserDto userDto = new UserDto();
    BeanUtils.copyProperties(user, userDto);
    return userDto;
  }

  public static User toUserFromUserDto(UserDto userDto) {
    User user = new User();
    BeanUtils.copyProperties(userDto, user);
    return user;
  }

  public static UserTransaction toUserTransactionFromTransactionRequestDto(TransactionRequestDto transactionRequestDto) {
    UserTransaction userTransaction = new UserTransaction();
    userTransaction.setUserId(transactionRequestDto.getUserId());
    userTransaction.setAmount(transactionRequestDto.getAmount());
    userTransaction.setTransactionDate(LocalDateTime.now());
    return userTransaction;
  }

  public static TransactionResponseDto toUserTransactionResponse(TransactionRequestDto transactionRequestDto,
      TransactionStatus transactionStatus) {
    TransactionResponseDto transactionResponseDto = new TransactionResponseDto();
    transactionResponseDto.setUserId(transactionRequestDto.getUserId());
    transactionResponseDto.setAmount(transactionRequestDto.getAmount());
    transactionResponseDto.setStatus(transactionStatus);
    return transactionResponseDto;
  }

  public static TransactionDto transactionDtoFromUserTransaction(UserTransaction userTransaction) {
    TransactionDto transactionDto = new TransactionDto();
    BeanUtils.copyProperties(userTransaction, transactionDto);
    return transactionDto;
  }

}
