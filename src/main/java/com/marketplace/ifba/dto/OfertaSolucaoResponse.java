package com.marketplace.ifba.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record OfertaSolucaoResponse(
        UUID idSolucao,
        String nome,
        String descricao,
        Integer prazo,
        String resumo,
        String status,
        Boolean aprovado,
        String tipoSolucao,
        String restricao,
        Double preco,
        String recursoNecessario,
        LocalDateTime dataAprovacao,
        LocalDateTime dataRegistro
) {
}
