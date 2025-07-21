package com.marketplace.ifba.dto;

import com.marketplace.ifba.model.enums.StatusProjeto;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusProjetoRequest(
        @NotNull(message = "O novo status do projeto é obrigatório.")
        StatusProjeto novoStatus
) {}