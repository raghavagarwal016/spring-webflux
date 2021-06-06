package com.spring.webflux.webfluxdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FailedValidationResponse {
 private int errorCode;
 private String errorMessage;
 private int value;
}
