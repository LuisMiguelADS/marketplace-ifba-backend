package com.marketplace.ifba.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.UUID;

public record DemandaRequest(
        @NotBlank(message = "O nome da demanda é obrigatório.")
        @Size(max = 255, message = "O nome não pode exceder 255 caracteres.")
        String nome,

        @NotBlank(message = "O e-mail do responsável é obrigatório.")
        @Email(message = "E-mail do responsável inválido.")
        String emailResponsavel,

        @NotNull(message = "O orçamento é obrigatório.")
        @Min(value = 0, message = "O orçamento não pode ser negativo.")
        Double orcamento,

        @NotBlank(message = "A descrição da demanda é obrigatória.")
        @Size(max = 2000, message = "A descrição não pode exceder 2000 caracteres.")
        String descricao,

        @NotBlank(message = "O resumo da demanda é obrigatório.")
        @Size(max = 500, message = "O resumo não pode exceder 500 caracteres.")
        String resumo,

        @NotBlank(message = "Os critérios da demanda são obrigatórios.")
        @Size(max = 1000, message = "Os critérios não podem exceder 1000 caracteres.")
        String criterio,

        // Status, aprovação e visualizações são gerenciados internamente

        @NotNull(message = "A data do prazo final da demanda é obrigatória.")
        @FutureOrPresent(message = "A data do prazo final deve ser no presente ou futuro.")
        LocalDateTime dataPrazoFinal,

        @NotNull(message = "O ID do usuário criador é obrigatório.")
        UUID idUsuarioCriador,

        @NotNull(message = "O ID da organização é obrigatório.")
        UUID idOrganizacao
) {}
