package com.marketplace.ifba.dto;

import com.marketplace.ifba.model.enums.StatusProposta;

import java.time.LocalDateTime;
import java.util.UUID;

public record PropostaResponse(
        UUID idProposta,
        GrupoPesquisaResponse grupoPesquisa,
        InstituicaoResponse instituicao,
        String nome,
        String solucao,
        String descricao,
        String resumo,
        Double orcamento,
        String restricoes,
        String recursosNecessarios,
        StatusProposta status,
        LocalDateTime dataPrazoProposta,
        LocalDateTime dataRegistro
) {}
