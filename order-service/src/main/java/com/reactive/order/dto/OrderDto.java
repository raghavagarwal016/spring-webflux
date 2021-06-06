package com.reactive.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDto {
  private int id;

  private String productId;

  private Integer userId;

  private Integer amount;

  private OrderStatus status;
}
