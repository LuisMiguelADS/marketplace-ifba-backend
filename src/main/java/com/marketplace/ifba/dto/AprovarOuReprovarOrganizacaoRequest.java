package com.marketplace.ifba.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AprovarOuReprovarOrganizacaoRequest(
        @NotNull(message = "O ID da organização é obrigatório para aprovação.")
        UUID idOrganizacao,

        @NotNull(message = "O ID do administrador aprovador é obrigatório.")
        UUID idAdm,
        Boolean decisao
) {}
