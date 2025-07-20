package com.marketplace.ifba.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record PropostaRequest(
        @NotNull(message = "O ID do grupo de pesquisa é obrigatório.")
        UUID idGrupoPesquisa,

        @NotNull(message = "O ID da instituição é obrigatório.")
        UUID idInstituicao,

        @NotBlank(message = "O nome da proposta é obrigatório.")
        @Size(max = 255, message = "O nome não pode exceder 255 caracteres.")
        String nome,

        @Size(max = 1000, message = "A solução não pode exceder 1000 caracteres.")
        String solucao,

        @Size(max = 2000, message = "A descrição não pode exceder 2000 caracteres.")
        String descricao,

        @Size(max = 500, message = "O resumo não pode exceder 500 caracteres.")
        String resumo,

        Double orcamento,

        @Size(max = 500, message = "As restrições não podem exceder 500 caracteres.")
        String restricoes,

        @Size(max = 500, message = "Os recursos necessários não podem exceder 500 caracteres.")
        String recursosNecessarios,

        @FutureOrPresent(message = "A data de prazo da proposta deve ser no presente ou futuro.")
        LocalDateTime dataPrazoProposta
) {}
