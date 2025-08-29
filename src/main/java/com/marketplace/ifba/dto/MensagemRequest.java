package com.marketplace.ifba.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record MensagemRequest(
        @NotBlank(message = "O conteúdo da mensagem é obrigatório.")
        @Size(max = 1000, message = "A mensagem não pode exceder 1000 caracteres.")
        String mensagem,

        @NotNull(message = "O ID do chat é obrigatório.")
        UUID idChat,

        @NotNull(message = "O ID do usuário é obrigatório.")
        UUID idUsuario
) {}