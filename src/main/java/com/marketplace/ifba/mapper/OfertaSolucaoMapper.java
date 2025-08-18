package com.marketplace.ifba.mapper;

import com.marketplace.ifba.dto.OfertaSolucaoRequest;
import com.marketplace.ifba.dto.OfertaSolucaoResponse;
import com.marketplace.ifba.model.OfertaSolucao;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OfertaSolucaoMapper {

    private final GrupoPesquisaMapper grupoPesquisaMapper;
    private final DemandaMapper demandaMapper;

    public OfertaSolucaoMapper(GrupoPesquisaMapper grupoPesquisaMapper, DemandaMapper demandaMapper) {
        this.grupoPesquisaMapper = grupoPesquisaMapper;
        this.demandaMapper = demandaMapper;
    }

    public OfertaSolucao toEntity(OfertaSolucaoRequest request) {
        if (request == null) {
            return null;
        }

        OfertaSolucao ofertaSolucao = new OfertaSolucao();
        ofertaSolucao.setNome(request.nome());
        ofertaSolucao.setDescricao(request.descricao());
        ofertaSolucao.setPrazo(request.prazo());
        ofertaSolucao.setResumo(request.resumo());
        ofertaSolucao.setTipoSolucao(request.tipoSolucao());
        ofertaSolucao.setRestricao(request.restricao());
        ofertaSolucao.setPreco(request.preco());
        ofertaSolucao.setRecursosNecessarios(request.recursosNecessarios());

        return ofertaSolucao;
    }

    public OfertaSolucaoResponse toDTO(OfertaSolucao ofertaSolucao) {
        if (ofertaSolucao == null) {
            return null;
        }

        return new OfertaSolucaoResponse(
                ofertaSolucao.getIdSolucao(),
                ofertaSolucao.getNome(),
                ofertaSolucao.getDescricao(),
                ofertaSolucao.getPrazo(),
                ofertaSolucao.getResumo(),
                ofertaSolucao.getStatus(),
                ofertaSolucao.getTipoSolucao(),
                ofertaSolucao.getRestricao(),
                ofertaSolucao.getPreco(),
                ofertaSolucao.getRecursosNecessarios(),
                ofertaSolucao.getDataAprovacao(),
                ofertaSolucao.getDataRegistro(),
                Optional.ofNullable(ofertaSolucao.getGrupoPesquisa()).map(grupoPesquisaMapper::toDTO).orElse(null),
                Optional.ofNullable(ofertaSolucao.getDemanda()).map(demandaMapper::toDTO).orElse(null)
        );
    }
}
