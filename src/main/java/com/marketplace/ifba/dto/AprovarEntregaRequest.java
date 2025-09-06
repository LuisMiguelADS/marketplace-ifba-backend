package com.marketplace.ifba.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AprovarEntregaRequest(
        @NotNull(message = "O ID da entrega é obrigatório para aprovação.")
        UUID idEntrega,
        @NotNull(message = "O ID do usuário aprovador é obrigatório.")
        UUID idUsuarioAprovador
) {}