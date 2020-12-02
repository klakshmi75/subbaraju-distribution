package com.lakshmi.poc.exception;

public class CustomException extends RuntimeException {
    public CustomException(String msg) {
        super(msg);
    }

    public CustomException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
