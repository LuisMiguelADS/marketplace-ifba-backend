package com.marketplace.ifba.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ProjetoRequest(
        @NotBlank(message = "O nome do projeto é obrigatório.")
        @Size(max = 255, message = "O nome não pode exceder 255 caracteres.")
        String nome,

        @NotNull(message = "O ID da organização é obrigatório.")
        UUID idOrganizacao,

        @NotNull(message = "O ID da instituição é obrigatório.")
        UUID idInstituicao,

        @NotNull(message = "O ID da demanda é obrigatório para um projeto.")
        UUID idDemanda,

        @NotNull(message = "O ID da oferta é obrigatório para um projeto.")
        UUID idOfertaSolucao,

        @NotNull(message = "O ID do grupo de pesquisa é obrigatório.")
        UUID idGrupoPesquisa
) {}