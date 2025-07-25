package com.marketplace.ifba.exception;

import java.util.UUID;

public class UsuarioInvalidoException extends RuntimeException {
    public UsuarioInvalidoException(UUID id) {
        super("Usuário não encontrado com ID: " + id);
    }
    public UsuarioInvalidoException(String message) {
        super(message);
    }
    public UsuarioInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }
}
