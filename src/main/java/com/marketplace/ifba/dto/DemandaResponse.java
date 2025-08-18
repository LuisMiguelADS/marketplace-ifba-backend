package com.marketplace.ifba.dto;

import com.marketplace.ifba.model.enums.StatusDemanda;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record DemandaResponse(
        UUID idDemanda,
        String nome,
        String emailResponsavel,
        Double orcamento,
        String descricao,
        String resumo,
        String criterio,
        StatusDemanda status,
        Integer visualizacoes,
        LocalDate dataPrazoFinal,
        LocalDate dataAprovado,
        UserResponse usuarioCriador,
        OrganizacaoResponse organizacao,
        List<GrupoPesquisaResponse> gruposPesquisa
) {}
