package com.spring.webflux.webfluxdemo.dto;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class ResponseDouble {
  private Date date = new Date();
  private double output;

  public ResponseDouble(double output) {
    this.output = output;
  }
}
