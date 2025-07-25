package com.marketplace.ifba.exception;

import java.util.UUID;

public class InstituicaoInvalidaException extends RuntimeException {
    public InstituicaoInvalidaException(UUID id) {
      super("Instituição não encontrada com ID: " + id);
    }
    public InstituicaoInvalidaException(String message) {
      super(message);
    }
    public InstituicaoInvalidaException(String message, Throwable cause) {
      super(message, cause);
    }
}
