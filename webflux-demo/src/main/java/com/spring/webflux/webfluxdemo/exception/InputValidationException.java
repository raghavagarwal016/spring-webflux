package com.spring.webflux.webfluxdemo.exception;

public class InputValidationException extends RuntimeException {

  private static final String message = "allowed range 10-20";
  private static final int errorCode = 300;
  private final int value;


  public InputValidationException(int value) {
    super(message);
    this.value = value;
  }

  public int getErrorCode() {
    return errorCode;
  }

  public int getValue() {
    return value;
  }
}
