package com.marketplace.ifba.dto;

import com.marketplace.ifba.model.enums.StatusProjeto;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AtualizarNomeProjetoRequest(
        @NotNull(message = "O id do projeto é obrigatório.")
        UUID idProjeto,
        @NotNull(message = "O novo nome do projeto é obrigatório.")
        String novoNome
) {
}
