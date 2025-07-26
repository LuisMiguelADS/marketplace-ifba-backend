package com.marketplace.ifba.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AprovarInstituicaoRequest(
        @NotNull(message = "O ID da instituição é obrigatório para aprovação.")
        UUID idInstituicao,

        @NotNull(message = "O ID do administrador aprovador é obrigatório.")
        UUID idAdmAprovador
) {}
