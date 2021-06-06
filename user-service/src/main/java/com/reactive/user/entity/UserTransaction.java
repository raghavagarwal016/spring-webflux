package com.reactive.user.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserTransaction {

  @Id
  private Integer id;

  private Integer userId;

  private Integer amount;

  private LocalDateTime transactionDate;

}
