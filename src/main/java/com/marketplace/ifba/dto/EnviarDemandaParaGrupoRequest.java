package com.marketplace.ifba.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record EnviarDemandaParaGrupoRequest(
        @NotNull(message = "ID da demanda é obrigatório")
        UUID idDemanda,
        
        @NotNull(message = "ID do grupo de pesquisa é obrigatório")
        UUID idGrupoPesquisa
) {}