package com.marketplace.ifba.exception;

public class MensagemInvalidaException extends RuntimeException {
    public MensagemInvalidaException(String message) {
        super(message);
    }
}