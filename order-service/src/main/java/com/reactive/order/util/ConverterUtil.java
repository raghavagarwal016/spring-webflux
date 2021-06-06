package com.reactive.order.util;

import org.springframework.beans.BeanUtils;

import com.reactive.order.dto.OrderDto;
import com.reactive.order.dto.OrderResponseDto;
import com.reactive.order.dto.OrderStatus;
import com.reactive.order.dto.RequestContext;
import com.reactive.order.dto.TransactionRequestDto;
import com.reactive.order.dto.TransactionStatus;
import com.reactive.order.entity.Order;

public class ConverterUtil {

  public static TransactionRequestDto transactionRequestDto(RequestContext requestContext) {
    System.out.println(requestContext);
    TransactionRequestDto transactionRequestDto = new TransactionRequestDto();
    transactionRequestDto.setUserId(requestContext.getOrderRequestDto().getUserId());
    transactionRequestDto.setAmount(requestContext.getProductDto().getPrice());
    requestContext.setTransactionRequestDto(transactionRequestDto);
    return transactionRequestDto;
  }

  public static Order toOrder(RequestContext requestContext) {
    Order order = new Order();
    order.setUserId(requestContext.getOrderRequestDto().getUserId());
    order.setProductId(requestContext.getProductDto().getId());
    order.setAmount(requestContext.getProductDto().getPrice());
    order.setStatus(TransactionStatus.APPROVED.equals(requestContext.getTransactionResponseDto().getStatus()) ?
        OrderStatus.COMPLETED :
        OrderStatus.FAILED);
    return order;
  }

  public static OrderResponseDto toOrderResponseDto(Order order) {
    OrderResponseDto orderResponseDto = new OrderResponseDto();
    BeanUtils.copyProperties(order, orderResponseDto);
    orderResponseDto.setOrderId(order.getId());
    return orderResponseDto;
  }

  public static Order toOrderFromOrderDto(OrderDto orderDto) {
    Order order = new Order();
    order.setUserId(orderDto.getUserId());
    order.setProductId(orderDto.getProductId());
    order.setAmount(orderDto.getAmount());
    order.setStatus(OrderStatus.COMPLETED);
    return order;
  }

  public static OrderDto toOrderDtoFromOrder(Order order) {
   OrderDto orderDto = new OrderDto();
   BeanUtils.copyProperties(order, orderDto);
   return orderDto;
  }

}
