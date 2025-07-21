package com.marketplace.ifba.dto;

import com.marketplace.ifba.model.enums.StatusProjeto;

import java.util.UUID;

public record ProjetoResponse(
        UUID idProjeto,
        String nome,
        StatusProjeto status,
        OrganizacaoResponse organizacao,
        InstituicaoResponse instituicao,
        DemandaResponse demanda,
        OfertaSolucaoResponse solucaoOferta,
        GrupoPesquisaResponse grupoPesquisa
        // ChatResponse chat
) {}
