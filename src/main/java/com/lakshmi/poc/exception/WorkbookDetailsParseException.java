package com.lakshmi.poc.exception;

public class WorkbookDetailsParseException extends CustomException {
    public WorkbookDetailsParseException(String msg) {

        super(msg);
    }

    public WorkbookDetailsParseException(String msg, Throwable cause) {

        super(msg, cause);
    }
}
