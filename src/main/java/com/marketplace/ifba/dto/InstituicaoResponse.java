package com.marketplace.ifba.dto;

import java.time.LocalDateTime;

public record InstituicaoResponse(
        String nome,
        String sigla,
        String cnpj,
        String tipoInstituicao,
        String setor,
        String telefone,
        String site,
        String status,
        String logoURL,
        String descricao,
        LocalDateTime dataRegistro,
        LocalDateTime dataAprovacao,
        LocalDateTime dataAtualizacao,
        String nomeAdmAprovacao,
        String nomeUsuarioRegistro
) {
}
