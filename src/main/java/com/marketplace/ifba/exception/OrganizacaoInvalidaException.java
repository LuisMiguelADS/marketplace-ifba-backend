package com.marketplace.ifba.exception;

import java.util.UUID;

public class OrganizacaoInvalidaException extends RuntimeException {
    public OrganizacaoInvalidaException(UUID id) {
        super("Organização não encontrada com ID: " + id);
    }
    public OrganizacaoInvalidaException(String message) {
        super(message);
    }
    public OrganizacaoInvalidaException(String message, Throwable cause) {
        super(message, cause);
    }
}
