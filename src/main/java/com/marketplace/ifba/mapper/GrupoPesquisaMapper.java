package com.marketplace.ifba.mapper;

import com.marketplace.ifba.dto.*;
import com.marketplace.ifba.model.GrupoPesquisa;
import com.marketplace.ifba.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class GrupoPesquisaMapper {

    private final InstituicaoMapper instituicaoMapper;
    private final UserMapper userMapper;
    private final AreaMapper areaMapper;

    public GrupoPesquisaMapper(InstituicaoMapper instituicaoMapper, UserMapper userMapper, AreaMapper areaMapper) {
        this.instituicaoMapper = instituicaoMapper;
        this.userMapper = userMapper;
        this.areaMapper = areaMapper;
    }

    public GrupoPesquisa toEntity(GrupoPesquisaRequest request) {
        if (request == null) {
            return null;
        }

        GrupoPesquisa grupoPesquisa = new GrupoPesquisa();
        grupoPesquisa.setNome(request.nome());
        grupoPesquisa.setDescricao(request.descricao());

        return grupoPesquisa;
    }

    public GrupoPesquisaResponse toDTO(GrupoPesquisa grupoPesquisa) {
        if (grupoPesquisa == null) {
            return null;
        }

        List<UserResponse> usuariosDTO = Optional.ofNullable(grupoPesquisa.getUsuarios())
                .orElseGet(ArrayList::new)
                .stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());

        List<AreaResponse> areasDTO = Optional.ofNullable(grupoPesquisa.getAreas())
                .orElseGet(ArrayList::new)
                .stream()
                .map(areaMapper::toDTO)
                .collect(Collectors.toList());

        InstituicaoResponse instituicaoDTO = Optional.ofNullable(grupoPesquisa.getInstituicao())
                .map(instituicaoMapper::toDTO)
                .orElse(null);

        UserResponse usuarioRegistrador = Optional.ofNullable(grupoPesquisa.getUsuarioRegistrador())
                .map(userMapper::toDTO)
                .orElse(null);

        return new GrupoPesquisaResponse(
                grupoPesquisa.getIdGrupoPesquisa(),
                grupoPesquisa.getNome(),
                grupoPesquisa.getDescricao(),
                grupoPesquisa.getTrabalhos(),
                grupoPesquisa.getClassificacao(),
                grupoPesquisa.getStatus(),
                grupoPesquisa.getDataRegistro(),
                instituicaoDTO,
                usuariosDTO,
                areasDTO,
                usuarioRegistrador
        );
    }
}
