package com.marketplace.ifba.mapper;

import com.marketplace.ifba.dto.OfertaSolucaoRequest;
import com.marketplace.ifba.dto.OfertaSolucaoResponse;
import com.marketplace.ifba.model.OfertaSolucao;
import org.springframework.stereotype.Component;

@Component
public class OfertaSolucaoMapper {
    public OfertaSolucao toEntity(OfertaSolucaoRequest request) {
        OfertaSolucao ofertaSolucao = new OfertaSolucao();
        ofertaSolucao.setNome(request.nome());
        ofertaSolucao.setDescricao(request.descricao());
        ofertaSolucao.setPrazo(request.prazo());
        ofertaSolucao.setResumo(request.resumo());
        ofertaSolucao.setTipoSolucao(request.tipoSolucao());
        ofertaSolucao.setRestricao(request.restricao());
        ofertaSolucao.setPreco(request.preco());
        ofertaSolucao.setRecursoNecessario(request.recursoNecessario());
        return ofertaSolucao;
    }

    public OfertaSolucaoResponse toDTO(OfertaSolucao ofertaSolucao) {
        return new OfertaSolucaoResponse(
                ofertaSolucao.getIdSolucao(),
                ofertaSolucao.getNome(),
                ofertaSolucao.getDescricao(),
                ofertaSolucao.getPrazo(),
                ofertaSolucao.getResumo(),
                ofertaSolucao.getStatus(),
                ofertaSolucao.getAprovado(),
                ofertaSolucao.getTipoSolucao(),
                ofertaSolucao.getRestricao(),
                ofertaSolucao.getPreco(),
                ofertaSolucao.getRecursoNecessario(),
                ofertaSolucao.getDataAprovacao(),
                ofertaSolucao.getDataRegistro()
        );
    }
}
