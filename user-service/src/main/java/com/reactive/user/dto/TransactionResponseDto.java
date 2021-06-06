package com.reactive.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TransactionResponseDto {

  private Integer userId;

  private Integer amount;

  private TransactionStatus status;
}
