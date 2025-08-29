package com.marketplace.ifba.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record EditarMensagemRequest(
        @NotNull(message = "O ID da mensagem é obrigatório.")
        UUID idMensagem,

        @NotBlank(message = "O novo conteúdo da mensagem é obrigatório.")
        @Size(max = 1000, message = "A mensagem não pode exceder 1000 caracteres.")
        String novaMensagem
) {}