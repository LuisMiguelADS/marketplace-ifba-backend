package com.marketplace.ifba.exception;

import java.util.UUID;

public class UsuarioInvalidoException extends RuntimeException {
    public UsuarioInvalidoException(String message) {
        super(message);
    }
}
