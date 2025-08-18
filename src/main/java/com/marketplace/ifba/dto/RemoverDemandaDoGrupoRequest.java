package com.marketplace.ifba.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RemoverDemandaDoGrupoRequest(
        @NotNull(message = "ID do grupo de pesquisa é obrigatório")
        UUID idGrupoPesquisa,
        
        @NotNull(message = "ID da demanda é obrigatório")
        UUID idDemanda
) {}