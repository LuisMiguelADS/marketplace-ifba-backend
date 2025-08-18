package com.marketplace.ifba.exception;

import java.util.UUID;

public class AreaInvalidaException extends RuntimeException {
    public AreaInvalidaException(UUID id) {
      super("AREA não encontrada com ID: " + id);
    }
    public AreaInvalidaException(String message) {
      super(message);
    }
    public AreaInvalidaException(String message, Throwable cause) {
      super(message, cause);
    }
}
