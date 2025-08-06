package com.marketplace.ifba.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record OfertaSolucaoRequest(
        @NotBlank(message = "O nome da solução é obrigatório.")
        @Size(max = 255, message = "O nome da solução não pode exceder 255 caracteres.")
        String nome,

        @NotBlank(message = "A descrição da solução é obrigatório.")
        @Size(max = 2000, message = "A descrição não pode exceder 2000 caracteres.")
        String descricao,

        @NotBlank(message = "O prazo da solução é obrigatório.")
        @Min(value = 0, message = "O prazo deve ser um valor positivo.")
        Integer prazo,

        @Size(max = 500, message = "O resumo não pode exceder 500 caracteres.")
        String resumo,

        @NotBlank(message = "O tipo da solução é obrigatório.")
        @Size(max = 50, message = "O tipo da solução não pode exceder 50 caracteres.")
        String tipoSolucao,

        @Size(max = 500, message = "A restrição não pode exceder 500 caracteres.")
        String restricao,

        @NotBlank(message = "O preço da solução é obrigatório.")
        Double preco,

        @Size(max = 500, message = "Os recursos necessários não podem exceder 500 caracteres.")
        String recursosNecessarios,

        UUID idDemanda,

        UUID idOfertaSolucao
) {
}
