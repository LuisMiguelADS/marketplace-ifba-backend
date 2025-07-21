package com.marketplace.ifba.dto;

import com.marketplace.ifba.model.enums.StatusDemanda;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusDemandaRequest(
        @NotNull(message = "O novo status da demanda é obrigatório.")
        StatusDemanda novoStatus
) {}
