package com.marketplace.ifba.mapper;

import com.marketplace.ifba.dto.EntregaRequest;
import com.marketplace.ifba.dto.EntregaResponse;
import com.marketplace.ifba.model.Entrega;
import org.springframework.stereotype.Component;

@Component
public class EntregaMapper {

    public Entrega toEntity(EntregaRequest request) {
        Entrega entrega = new Entrega();
        entrega.setTitulo(request.titulo());
        entrega.setDescricao(request.descricao());
        entrega.setPrazoDesejado(request.prazoDesejado());
        return entrega;
    }

    public EntregaResponse toDTO(Entrega entrega) {
        return new EntregaResponse(
                entrega.getIdEntrega(),
                entrega.getTitulo(),
                entrega.getDescricao(),
                entrega.getPrazoDesejado(),
                entrega.getDataCriacao(),
                entrega.getStatus(),
                entrega.getProjeto() != null ? entrega.getProjeto().getIdProjeto() : null,
                entrega.getOrganizacaoSolicitante() != null ? entrega.getOrganizacaoSolicitante().getIdOrganizacao() : null,
                entrega.getOrganizacaoSolicitante() != null ? entrega.getOrganizacaoSolicitante().getNome() : null,
                entrega.getGrupoPesquisaSolicitante() != null ? entrega.getGrupoPesquisaSolicitante().getIdGrupoPesquisa() : null,
                entrega.getGrupoPesquisaSolicitante() != null ? entrega.getGrupoPesquisaSolicitante().getNome() : null,
                entrega.getOrganizacaoSolicitada() != null ? entrega.getOrganizacaoSolicitada().getIdOrganizacao() : null,
                entrega.getOrganizacaoSolicitada() != null ? entrega.getOrganizacaoSolicitada().getNome() : null,
                entrega.getGrupoPesquisaSolicitado() != null ? entrega.getGrupoPesquisaSolicitado().getIdGrupoPesquisa() : null,
                entrega.getGrupoPesquisaSolicitado() != null ? entrega.getGrupoPesquisaSolicitado().getNome() : null
        );
    }
}