package com.marketplace.ifba.dto;

import com.marketplace.ifba.model.enums.StatusSolicitacao;

import java.util.UUID;

public record SolicitacaoResponse(
        UUID idSolicitacao,
        UUID idUserApplicant,
        UUID idOrganizacaoRequested,
        StatusSolicitacao status
) {
}
