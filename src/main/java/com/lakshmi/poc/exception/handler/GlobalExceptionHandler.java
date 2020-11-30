package com.lakshmi.poc.exception.handler;

import com.google.common.collect.Lists;
import com.lakshmi.poc.constants.RequestConstants;
import com.lakshmi.poc.model.ErrorResponse;
import com.sun.javaws.exceptions.InvalidArgumentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("HttpMessageNotReadableException", ex);

        return new ErrorResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()),
                Arrays.asList(String.format("Error in parsing input | Error: %s", ex.getCause().getMessage())));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ErrorResponse handleBindExceptions(BindException ex) {
        log.error("BindException", ex);

        return processBindingResult(ex.getBindingResult());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException", ex);

        return processBindingResult(ex.getBindingResult());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidArgumentException.class)
    public ErrorResponse handleInvalidArgumentException(InvalidArgumentException ex) {
        log.error("InvalidArgumentException", ex);

        return new ErrorResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()), Arrays.asList(ex.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ErrorResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.error("MethodArgumentTypeMismatchException", ex);
        String message;
        if(LocalDate.class.isAssignableFrom(ex.getParameter().getParameterType())) {
            message = String.format("Invalid date format. Valid format is : %s", RequestConstants.DATE_FORMAT);
        } else {
            message = "Invalid Input";
        }

        return new ErrorResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()), Arrays.asList(ex.getMessage()));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleInvalidArgumentException(Exception ex) {
        log.error("Unexpected Exception", ex);

        return new ErrorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), Arrays.asList(ex.getMessage()));
    }


    private ErrorResponse processBindingResult(BindingResult result) {
        List<String> errors = Lists.newArrayList();
        errors.addAll(processFiledErrors(result));
        errors.addAll(processGlobalErrors(result));

        return new ErrorResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()), errors);
    }

    private List<String> processFiledErrors(BindingResult result) {
        return result.getFieldErrors().stream()
                .map(error -> String.format("Error in field validation | Field: %s | Error: %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
    }

    private List<String> processGlobalErrors(BindingResult result) {
        return result.getGlobalErrors().stream()
                .map(error -> String.format("Error in validation | Error: %s", error.getDefaultMessage()))
                .collect(Collectors.toList());
    }
}

