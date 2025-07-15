package com.marketplace.ifba.dto;

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
        String status,
        String logoURL,
        String descricao,
        LocalDateTime dataRegistro,
        LocalDateTime dataAprovacao,
        UserResponse admAprovacao, // Usando UserResponse
        UserResponse usuarioRegistro // Usando UserResponse
) {}
