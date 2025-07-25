package com.marketplace.ifba.exception;

import java.util.UUID;

public class DemandaInvalidaException extends RuntimeException {
    public DemandaInvalidaException(UUID id) {
        super("Demanda não encontrada com ID: " + id);
    }
    public DemandaInvalidaException(String message) {
        super(message);
    }
    public DemandaInvalidaException(String message, Throwable cause) {
        super(message, cause);
    }
}
