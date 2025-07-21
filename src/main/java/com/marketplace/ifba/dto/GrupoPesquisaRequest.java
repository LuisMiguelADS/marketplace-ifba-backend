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

        @PositiveOrZero(message = "O número de trabalhos deve ser positivo ou zero.")
        Integer trabalhos,

        @DecimalMin(value = "0.0", message = "A classificação mínima é 0.0.")
        @DecimalMax(value = "5.0", message = "A classificação máxima é 5.0.")
        Double classificacao,

        @NotNull(message = "O ID da instituição é obrigatório.")
        UUID idInstituicao,

        List<UUID> idsUsuarios,

        List<UUID> idsTags
) {}
