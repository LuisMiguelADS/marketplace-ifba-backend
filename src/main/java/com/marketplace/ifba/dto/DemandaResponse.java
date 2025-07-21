package com.marketplace.ifba.dto;

import com.marketplace.ifba.model.enums.StatusDemanda;

import java.time.LocalDateTime;
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
        Boolean aprovacaoDemandante,
        Integer vizualizacoes,
        LocalDateTime dataPrazoFinal,
        LocalDateTime dataAprovado,
        UserResponse usuarioCriador,
        OrganizacaoResponse organizacao
) {}
