package com.marketplace.ifba.exception;

import java.util.UUID;

public class ProjetoInvalidoException extends RuntimeException {
    public ProjetoInvalidoException(UUID id) {
        super("Usuário não encontrado com ID: " + id);
    }
    public ProjetoInvalidoException(String message) {
        super(message);
    }
    public ProjetoInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }
}
