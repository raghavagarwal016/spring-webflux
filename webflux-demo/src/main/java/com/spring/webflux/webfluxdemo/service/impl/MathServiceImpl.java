package com.spring.webflux.webfluxdemo.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.spring.webflux.webfluxdemo.dto.Response;
import com.spring.webflux.webfluxdemo.service.MathService;
import com.spring.webflux.webfluxdemo.util.ServiceUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MathServiceImpl implements MathService {

  // return square of a number
  @Override
  public Response findSquare(int input) {
    return new Response(input * input);
  }

  //calculate the table of a number with sleep time of 1sec between each number
  @Override
  public List<Response> multiplicationTable(int input) {
    return IntStream
        .rangeClosed(1, 10)
        .peek(i -> ServiceUtil.sleepSeconds(1))
        .peek(i -> log.info("math-service-multiplication: " + i))
        .mapToObj(i -> new Response(i * input))
        .collect(Collectors.toList());
  }

}
