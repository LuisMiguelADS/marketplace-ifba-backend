package com.marketplace.ifba.dto;

import com.marketplace.ifba.model.enums.StatusOrganizacao;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrganizacaoResponse(
        UUID idOrganizacao,
        String nome,
        String sigla,
        String cnpj,
        String tipoOrganizacao,
        String setor,
        String telefone,
        String site,
        StatusOrganizacao status,
        String descricao,
        LocalDateTime dataRegistro,
        LocalDateTime dataAprovacao,
        UserResponse admAprovacao,
        UserResponse usuarioRegistro
) {}
