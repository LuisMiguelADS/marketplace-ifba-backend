package com.marketplace.ifba.mapper;

import com.marketplace.ifba.dto.DemandaRequest;
import com.marketplace.ifba.dto.DemandaResponse;
import com.marketplace.ifba.model.Demanda;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DemandaMapper {

    private final UserMapper userMapper;
    private final OrganizacaoMapper organizacaoMapper;
    private final GrupoPesquisaMapper grupoPesquisaMapper;

    public DemandaMapper(UserMapper userMapper, OrganizacaoMapper organizacaoMapper, GrupoPesquisaMapper grupoPesquisaMapper) {
        this.userMapper = userMapper;
        this.organizacaoMapper = organizacaoMapper;
        this.grupoPesquisaMapper = grupoPesquisaMapper;
    }

    public Demanda toEntity(DemandaRequest request) {
        if (request == null) {
            return null;
        }

        Demanda demanda = new Demanda();
        demanda.setNome(request.nome());
        demanda.setEmailResponsavel(request.emailResponsavel());
        demanda.setOrcamento(request.orcamento());
        demanda.setDescricao(request.descricao());
        demanda.setResumo(request.resumo());
        demanda.setCriterio(request.criterio());
        demanda.setDataPrazoFinal(request.dataPrazoFinal());

        return demanda;
    }

    public DemandaResponse toDTO(Demanda demanda) {
        if (demanda == null) {
            return null;
        }

        return new DemandaResponse(
                demanda.getIdDemanda(),
                demanda.getNome(),
                demanda.getEmailResponsavel(),
                demanda.getOrcamento(),
                demanda.getDescricao(),
                demanda.getResumo(),
                demanda.getCriterio(),
                demanda.getStatus(),
                demanda.getVisualizacoes(),
                demanda.getDataPrazoFinal(),
                demanda.getDataAprovado(),
                Optional.ofNullable(demanda.getUsuarioRegistrador()).map(userMapper::toDTO).orElse(null),
                Optional.ofNullable(demanda.getOrganizacao()).map(organizacaoMapper::toDTO).orElse(null),
                Optional.ofNullable(demanda.getGruposPesquisa())
                        .orElseGet(ArrayList::new)
                        .stream()
                        .map(grupoPesquisaMapper::toDTO)
                        .collect(Collectors.toList())
        );
    }
}
