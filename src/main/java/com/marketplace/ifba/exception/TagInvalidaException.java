package com.marketplace.ifba.exception;

import java.util.UUID;

public class TagInvalidaException extends RuntimeException {
    public TagInvalidaException(UUID id) {
      super("TAG não encontrada com ID: " + id);
    }
    public TagInvalidaException(String message) {
      super(message);
    }
    public TagInvalidaException(String message, Throwable cause) {
      super(message, cause);
    }
}
