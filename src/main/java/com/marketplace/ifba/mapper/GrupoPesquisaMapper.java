package com.marketplace.ifba.mapper;

import com.marketplace.ifba.dto.GrupoPesquisaRequest;
import com.marketplace.ifba.dto.GrupoPesquisaResponse;
import com.marketplace.ifba.dto.TagResponse;
import com.marketplace.ifba.dto.UserResponse;
import com.marketplace.ifba.model.GrupoPesquisa;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class GrupoPesquisaMapper {

    private final InstituicaoMapper instituicaoMapper;
    private final UserMapper userMapper;
    private final TagMapper tagMapper;

    public GrupoPesquisaMapper(InstituicaoMapper instituicaoMapper, UserMapper userMapper, TagMapper tagMapper) {
        this.instituicaoMapper = instituicaoMapper;
        this.userMapper = userMapper;
        this.tagMapper = tagMapper;
    }

    public GrupoPesquisa toEntity(GrupoPesquisaRequest request) {
        if (request == null) {
            return null;
        }

        GrupoPesquisa grupoPesquisa = new GrupoPesquisa();
        grupoPesquisa.setNome(request.nome());
        grupoPesquisa.setDescricao(request.descricao());
        grupoPesquisa.setTrabalhos(request.trabalhos());
        grupoPesquisa.setClassificacao(request.classificacao());

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

        List<TagResponse> tagsDTO = Optional.ofNullable(grupoPesquisa.getTags())
                .orElseGet(ArrayList::new)
                .stream()
                .map(tagMapper::toDTO)
                .collect(Collectors.toList());

        return new GrupoPesquisaResponse(
                grupoPesquisa.getIdGrupoPesquisa(),
                grupoPesquisa.getNome(),
                grupoPesquisa.getDescricao(),
                grupoPesquisa.getTrabalhos(),
                grupoPesquisa.getClassificacao(),
                grupoPesquisa.getStatus(),
                grupoPesquisa.getDataRegistro(),
                Optional.ofNullable(grupoPesquisa.getInstituicao())
                        .map(instituicaoMapper::toDTO)
                        .orElse(null),
                usuariosDTO,
                tagsDTO
        );
    }

    public void updateEntityFromRequest(GrupoPesquisaRequest request, GrupoPesquisa grupoPesquisa) {
        if (request == null || grupoPesquisa == null) {
            return;
        }

        Optional.ofNullable(request.nome()).ifPresent(grupoPesquisa::setNome);
        Optional.ofNullable(request.descricao()).ifPresent(grupoPesquisa::setDescricao);
        Optional.ofNullable(request.trabalhos()).ifPresent(grupoPesquisa::setTrabalhos);
        Optional.ofNullable(request.classificacao()).ifPresent(grupoPesquisa::setClassificacao);
    }
}
