package com.marketplace.ifba.dto;

import jakarta.validation.constraints.NotBlank;

public record AreaRequest(
        @NotBlank(message = "O nome é obrigatório.")
        String nomeTag
) {
}
