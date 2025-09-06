package com.marketplace.ifba.dto;

import com.marketplace.ifba.model.enums.StatusProjeto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ProjetoResponse(
        UUID idProjeto,
        String nome,
        LocalDate dataInicio,
        LocalDate dataFinal,
        StatusProjeto status,
        OrganizacaoResponse organizacao,
        InstituicaoResponse instituicao,
        DemandaResponse demanda,
        OfertaSolucaoResponse solucaoOferta,
        GrupoPesquisaResponse grupoPesquisa,
        List<EntregaResponse> entregas,
        UUID idChat
) {}
