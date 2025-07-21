package com.marketplace.ifba.mapper;

import com.marketplace.ifba.dto.OfertaSolucaoRequest;
import com.marketplace.ifba.dto.OfertaSolucaoResponse;
import com.marketplace.ifba.model.OfertaSolucao;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OfertaSolucaoMapper {

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
        ofertaSolucao.setRecursoNecessario(request.recursoNecessario());

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
                ofertaSolucao.getAprovado(),
                ofertaSolucao.getTipoSolucao(),
                ofertaSolucao.getRestricao(),
                ofertaSolucao.getPreco(),
                ofertaSolucao.getRecursoNecessario(),
                ofertaSolucao.getDataAprovacao(),
                ofertaSolucao.getDataRegistro()
        );
    }

    public void updateEntityFromRequest(OfertaSolucaoRequest request, OfertaSolucao ofertaSolucao) {
        if (request == null || ofertaSolucao == null) {
            return;
        }

        Optional.ofNullable(request.nome()).ifPresent(ofertaSolucao::setNome);
        Optional.ofNullable(request.descricao()).ifPresent(ofertaSolucao::setDescricao);
        Optional.ofNullable(request.prazo()).ifPresent(ofertaSolucao::setPrazo);
        Optional.ofNullable(request.resumo()).ifPresent(ofertaSolucao::setResumo);
        Optional.ofNullable(request.tipoSolucao()).ifPresent(ofertaSolucao::setTipoSolucao);
        Optional.ofNullable(request.restricao()).ifPresent(ofertaSolucao::setRestricao);
        Optional.ofNullable(request.preco()).ifPresent(ofertaSolucao::setPreco);
        Optional.ofNullable(request.recursoNecessario()).ifPresent(ofertaSolucao::setRecursoNecessario);
    }
}
