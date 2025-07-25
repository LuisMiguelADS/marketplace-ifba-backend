package com.marketplace.ifba.exception;

import java.util.UUID;

public class OfertaSolucaoInvalidaException extends RuntimeException {
    public OfertaSolucaoInvalidaException(UUID id) {
      super("Oferta não encontrada com ID: " + id);
    }
    public OfertaSolucaoInvalidaException(String message) {
      super(message);
    }
    public OfertaSolucaoInvalidaException(String message, Throwable cause) {
      super(message, cause);
    }
}
