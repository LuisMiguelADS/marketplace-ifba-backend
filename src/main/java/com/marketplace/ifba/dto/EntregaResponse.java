package com.marketplace.ifba.dto;

import com.marketplace.ifba.model.enums.StatusEntrega;
import java.time.LocalDateTime;
import java.util.UUID;

public record EntregaResponse(
        UUID idEntrega,
        String titulo,
        String descricao,
        LocalDateTime prazoDesejado,
        LocalDateTime dataCriacao,
        StatusEntrega status,
        UUID idProjeto,
        UUID idOrganizacaoSolicitante,
        String nomeOrganizacaoSolicitante,
        UUID idGrupoPesquisaSolicitante,
        String nomeGrupoPesquisaSolicitante,
        UUID idOrganizacaoSolicitada,
        String nomeOrganizacaoSolicitada,
        UUID idGrupoPesquisaSolicitado,
        String nomeGrupoPesquisaSolicitado
) {
}