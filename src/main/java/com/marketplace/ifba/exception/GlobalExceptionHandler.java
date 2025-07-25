package com.marketplace.ifba.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {
    public GlobalExceptionHandler(String message) {
        super(message);
    }
}
