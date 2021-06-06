package com.spring.webflux.webfluxdemo.router;

import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.spring.webflux.webfluxdemo.dto.FailedValidationResponse;
import com.spring.webflux.webfluxdemo.exception.InputValidationException;
import com.spring.webflux.webfluxdemo.handler.RequestHandler;

import reactor.core.publisher.Mono;

@Configuration
public class RouterConfig {

  @Autowired
  private RequestHandler requestHandler;

  @Bean
  public RouterFunction<ServerResponse> serverResponseRouterFunction() {
    return RouterFunctions.route().GET("route/square/{input}", requestHandler::squareHandler)
        .GET("/route/muliptication-table/{input}", requestHandler::multiplicationHandler)
        .GET("/route/streaming/muliptication-table/{input}", requestHandler::multiplicationStreamingHandler)
        .POST("/route/multiply", requestHandler::multiplyHandler)
        .GET("/route/validation/square/{input}", requestHandler::squareHandlerWebValidation)
        .GET("/route/predicate/square/{input}", RequestPredicates.path("*/1?"),
            requestHandler::squareHandler)// request predicate. Can be based of header params etc.
        .GET("/route/predicate/square/{input}", serverRequest -> ServerResponse.badRequest().bodyValue("10-19 allowed"))
        .onError(InputValidationException.class, exceptionHandler()).build();
  }

  @Bean
  public RouterFunction<ServerResponse> calculatorRouterFunction() {
    return RouterFunctions.route()
        .GET("/calculate/{first}/{second}", requestHandler::calculate)
        .onError(RuntimeException.class, calculatorExceptionHandler())
        .build();
  }

  private BiFunction<Throwable, ServerRequest, Mono<ServerResponse>> exceptionHandler() {
    return (throwable, serverRequest) -> {
      InputValidationException inputValidationException = (InputValidationException) throwable;
      FailedValidationResponse failedValidationResponse = new FailedValidationResponse();
      failedValidationResponse.setValue(inputValidationException.getValue());
      failedValidationResponse.setErrorCode(inputValidationException.getErrorCode());
      failedValidationResponse.setErrorMessage(inputValidationException.getMessage());
      return ServerResponse.badRequest().bodyValue(failedValidationResponse);
    };
  }

  private BiFunction<Throwable, ServerRequest, Mono<ServerResponse>> calculatorExceptionHandler() {
    return (throwable, serverRequest) -> {
      RuntimeException runtimeException = (RuntimeException) throwable;
      FailedValidationResponse failedValidationResponse = new FailedValidationResponse();
      failedValidationResponse.setErrorMessage(runtimeException.getMessage());
      return ServerResponse.badRequest().bodyValue(failedValidationResponse);
    };
  }

}
