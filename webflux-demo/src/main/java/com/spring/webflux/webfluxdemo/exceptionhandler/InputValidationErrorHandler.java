package com.spring.webflux.webfluxdemo.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.spring.webflux.webfluxdemo.dto.FailedValidationResponse;
import com.spring.webflux.webfluxdemo.exception.InputValidationException;

@ControllerAdvice
public class InputValidationErrorHandler {

  @ExceptionHandler(InputValidationException.class)
  public ResponseEntity<FailedValidationResponse> hanleException(InputValidationException e) {
    FailedValidationResponse failedValidationResponse = new FailedValidationResponse();
    failedValidationResponse.setErrorCode(e.getErrorCode());
    failedValidationResponse.setErrorMessage(e.getMessage());
    failedValidationResponse.setValue(e.getValue());
    return new ResponseEntity<>(failedValidationResponse, HttpStatus.BAD_REQUEST);
  }


}
