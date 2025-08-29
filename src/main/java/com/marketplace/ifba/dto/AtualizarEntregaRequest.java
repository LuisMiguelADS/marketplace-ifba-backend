package com.marketplace.ifba.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record AtualizarEntregaRequest(
        @NotNull(message = "ID da entrega é obrigatório")
        UUID idEntrega,

        @NotBlank(message = "Título é obrigatório")
        @Size(max = 255, message = "Título deve ter no máximo 255 caracteres")
        String titulo,

        @Size(max = 2000, message = "Descrição deve ter no máximo 2000 caracteres")
        String descricao,

        LocalDateTime prazoDesejado
) {
}