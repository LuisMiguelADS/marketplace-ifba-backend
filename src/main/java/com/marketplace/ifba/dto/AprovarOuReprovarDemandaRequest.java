package com.marketplace.ifba.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AprovarOuReprovarDemandaRequest(
        @NotNull(message = "O ID da demanda é obrigatório.")
        UUID idDemanda,

        @NotNull(message = "A aprovação do demandante é obrigatória.")
        Boolean decisao
) {}
