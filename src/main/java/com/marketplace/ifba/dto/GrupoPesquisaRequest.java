package com.marketplace.ifba.dto;

import jakarta.validation.constraints.*;

import java.util.List;
import java.util.UUID;

public record GrupoPesquisaRequest(
        @NotBlank(message = "O nome do grupo de pesquisa é obrigatório.")
        @Size(max = 50, message = "O nome não pode exceder 50 caracteres.")
        String nome,

        @Size(max = 2000, message = "A descrição não pode exceder 2000 caracteres.")
        String descricao,

        @NotNull(message = "O ID da instituição é obrigatório.")
        UUID idInstituicao,

        List<UUID> idsAreas,

        @NotNull(message = "O ID do usuário registrador é obrigatório.")
        UUID usuarioRegistrador,

        UUID idGrupoPesquisa
) {}
