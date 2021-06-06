package com.spring.webflux.webfluxdemo.service;

import com.spring.webflux.webfluxdemo.dto.Response;
import java.util.List;

public interface MathService {

  Response findSquare(int input);

  List<Response> multiplicationTable(int input);

}
