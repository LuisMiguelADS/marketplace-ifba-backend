package com.marketplace.ifba.dto;

import jakarta.validation.constraints.NotBlank;

public record TagRequest(
        @NotBlank(message = "O nome é obrigatório.")
        String nomeTag
) {
}
