package com.reactive.user.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransactionDto {
  private Integer id;

  private Integer userId;

  private Integer amount;

  private LocalDateTime transactionDate;
}
