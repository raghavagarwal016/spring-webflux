package com.reactive.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderResponseDto {
  private Integer orderId;

  private Integer userId;

  private String productId;

  private Integer amount;

  private OrderStatus status;
}
