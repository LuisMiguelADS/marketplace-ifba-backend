package com.marketplace.ifba.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AprovaOuReprovaOfertaSolucaoRequest(
        @NotNull
        UUID idOfertaSolucao,
        @NotNull
        Boolean decisao
) {
}
