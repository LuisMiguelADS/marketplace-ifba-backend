package com.marketplace.ifba.exception;

import java.util.UUID;

public class PropostaInvalidaException extends RuntimeException {
    public PropostaInvalidaException(UUID id) {
        super("Usuário não encontrado com ID: " + id);
    }
    public PropostaInvalidaException(String message) {
        super(message);
    }
    public PropostaInvalidaException(String message, Throwable cause) {
        super(message, cause);
    }
}
