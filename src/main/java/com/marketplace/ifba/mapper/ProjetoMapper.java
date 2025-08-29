package com.marketplace.ifba.mapper;

import com.marketplace.ifba.dto.ProjetoRequest;
import com.marketplace.ifba.dto.ProjetoResponse;
import com.marketplace.ifba.model.Projeto;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProjetoMapper {

    private final OrganizacaoMapper organizacaoMapper;
    private final InstituicaoMapper instituicaoMapper;
    private final DemandaMapper demandaMapper;
    private final OfertaSolucaoMapper ofertaSolucaoMapper;
    private final GrupoPesquisaMapper grupoPesquisaMapper;
    private final EntregaMapper entregaMapper;

    public ProjetoMapper(OrganizacaoMapper organizacaoMapper, InstituicaoMapper instituicaoMapper,
                         DemandaMapper demandaMapper, OfertaSolucaoMapper ofertaSolucaoMapper,
                         GrupoPesquisaMapper grupoPesquisaMapper, EntregaMapper entregaMapper) {
        this.organizacaoMapper = organizacaoMapper;
        this.instituicaoMapper = instituicaoMapper;
        this.demandaMapper = demandaMapper;
        this.ofertaSolucaoMapper = ofertaSolucaoMapper;
        this.grupoPesquisaMapper = grupoPesquisaMapper;
        this.entregaMapper = entregaMapper;
    }

    public Projeto toEntity(ProjetoRequest request) {
        if (request == null) {
            return null;
        }

        Projeto projeto = new Projeto();
        projeto.setNome(request.nome());
        return projeto;
    }

    public ProjetoResponse toDTO(Projeto projeto) {
        if (projeto == null) {
            return null;
        }

        return new ProjetoResponse(
                projeto.getIdProjeto(),
                projeto.getNome(),
                projeto.getStatus(),
                Optional.ofNullable(projeto.getOrganizacao()).map(organizacaoMapper::toDTO).orElse(null),
                Optional.ofNullable(projeto.getInstituicao()).map(instituicaoMapper::toDTO).orElse(null),
                Optional.ofNullable(projeto.getDemanda()).map(demandaMapper::toDTO).orElse(null),
                Optional.ofNullable(projeto.getOfertaSolucao()).map(ofertaSolucaoMapper::toDTO).orElse(null),
                Optional.ofNullable(projeto.getGrupoPesquisa()).map(grupoPesquisaMapper::toDTO).orElse(null),
                Optional.ofNullable(projeto.getEntregas())
                        .map(entregas -> entregas.stream().map(entregaMapper::toDTO).toList())
                        .orElse(null),
                projeto.getChat().getIdChat()        
        );
    }

}