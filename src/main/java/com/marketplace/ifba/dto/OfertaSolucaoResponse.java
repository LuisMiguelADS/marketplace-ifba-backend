package com.marketplace.ifba.dto;

import com.marketplace.ifba.model.enums.StatusOfertaSolucao;

import java.time.LocalDateTime;
import java.util.UUID;

public record OfertaSolucaoResponse(
        UUID idSolucao,
        String nome,
        String descricao,
        Integer prazo,
        String resumo,
        StatusOfertaSolucao status,
        String tipoSolucao,
        String restricao,
        Double preco,
        String recursoNecessario,
        LocalDateTime dataAprovacao,
        LocalDateTime dataRegistro,
        GrupoPesquisaResponse grupoPesquisa,
        DemandaResponse demanda
) {
}
