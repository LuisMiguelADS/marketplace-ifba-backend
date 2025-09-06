package com.marketplace.ifba.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record EntregaRequest(
        @NotBlank(message = "Título é obrigatório")
        @Size(max = 255, message = "Título deve ter no máximo 255 caracteres")
        String titulo,

        @Size(max = 2000, message = "Descrição deve ter no máximo 2000 caracteres")
        String descricao,

        LocalDate prazoDesejado,

        @NotNull(message = "ID do projeto é obrigatório")
        UUID idProjeto,

        UUID idOrganizacaoSolicitante,
        UUID idGrupoPesquisaSolicitante,

        UUID idOrganizacaoSolicitada,
        UUID idGrupoPesquisaSolicitado,

        UUID idUsuarioSolicitante
) {
}