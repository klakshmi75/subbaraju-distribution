package com.lakshmi.poc.exception.handler;

public class ExcelDetailsParseException extends RuntimeException {
    public ExcelDetailsParseException(String msg) {
        super(msg);
    }
    public ExcelDetailsParseException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
