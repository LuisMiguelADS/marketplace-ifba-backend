package com.marketplace.ifba.dto;

import com.marketplace.ifba.model.User;
import com.marketplace.ifba.model.enums.StatusInstituicao;

import java.time.LocalDateTime;
import java.util.UUID;

public record InstituicaoResponse(
        UUID idInstituicao,
        String nome,
        String sigla,
        String cnpj,
        String tipoInstituicao,
        String setor,
        String telefone,
        String site,
        StatusInstituicao status,
        String logoURL,
        String descricao,
        LocalDateTime dataRegistro,
        LocalDateTime dataAprovacao,
        LocalDateTime dataAtualizacao,
        UserResponse admAprovacao,
        UserResponse usuarioRegistro
) {
}
