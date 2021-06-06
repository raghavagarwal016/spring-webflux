package com.reactive.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RequestContext {
  private OrderRequestDto orderRequestDto;
  private ProductDto productDto;
  private TransactionRequestDto transactionRequestDto;
  private TransactionResponseDto transactionResponseDto;

  public RequestContext(OrderRequestDto orderRequestDto) {
    this.orderRequestDto = orderRequestDto;
  }
}
