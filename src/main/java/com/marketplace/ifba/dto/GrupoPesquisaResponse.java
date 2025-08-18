package com.marketplace.ifba.dto;

import com.marketplace.ifba.model.User;
import com.marketplace.ifba.model.enums.StatusGrupoPesquisa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record GrupoPesquisaResponse(
        UUID idGrupoPesquisa,
        String nome,
        String descricao,
        Integer trabalhos,
        Double classificacao,
        StatusGrupoPesquisa status,
        LocalDateTime dataRegistro,
        InstituicaoResponse instituicao,
        List<UserResponse> usuarios,
        List<AreaResponse> areas,
        UserResponse usuarioRegistrador
) {}
