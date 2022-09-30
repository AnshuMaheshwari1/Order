package com.anshu.exception;

public class OrderException extends RuntimeException {
  public OrderException(String errorMessage, Throwable err) {
      super(errorMessage, err);
  }
}
