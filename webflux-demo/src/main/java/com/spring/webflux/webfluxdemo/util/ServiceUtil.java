package com.spring.webflux.webfluxdemo.util;

public class ServiceUtil {

  public static void sleepSeconds(int seconds) {
    try {
      Thread.sleep(seconds * 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
