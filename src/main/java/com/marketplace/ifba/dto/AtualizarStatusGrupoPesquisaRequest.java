package com.marketplace.ifba.dto;

import com.marketplace.ifba.model.enums.StatusGrupoPesquisa;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AtualizarStatusGrupoPesquisaRequest(
        @NotNull(message = "O ID do grupo de pesquisa é obrigatório.")
        UUID idGrupoPesquisa,

        @NotNull(message = "O novo status é obrigatório.")
        StatusGrupoPesquisa novoStatus
) {}
