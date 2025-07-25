package com.marketplace.ifba.exception;

import java.util.UUID;

public class GrupoPesquisaInvalidoException extends RuntimeException {
    public GrupoPesquisaInvalidoException(UUID id) {
        super("Usuário não encontrado com ID: " + id);
    }
    public GrupoPesquisaInvalidoException(String message) {
        super(message);
    }
    public GrupoPesquisaInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }
}
