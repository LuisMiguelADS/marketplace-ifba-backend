package com.marketplace.ifba.mapper;

import com.marketplace.ifba.dto.PropostaRequest;
import com.marketplace.ifba.dto.PropostaResponse;
import com.marketplace.ifba.model.Proposta;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PropostaMapper {

    private final GrupoPesquisaMapper grupoPesquisaMapper;
    private final InstituicaoMapper instituicaoMapper;

    public PropostaMapper(GrupoPesquisaMapper grupoPesquisaMapper, InstituicaoMapper instituicaoMapper) {
        this.grupoPesquisaMapper = grupoPesquisaMapper;
        this.instituicaoMapper = instituicaoMapper;
    }

    public Proposta toEntity(PropostaRequest request) {
        if (request == null) {
            return null;
        }

        Proposta proposta = new Proposta();
        proposta.setNome(request.nome());
        proposta.setSolucao(request.solucao());
        proposta.setDescricao(request.descricao());
        proposta.setResumo(request.resumo());
        proposta.setOrcamento(request.orcamento());
        proposta.setRestricoes(request.restricoes());
        proposta.setRecursosNecessarios(request.recursosNecessarios());
        proposta.setDataPrazoProposta(request.dataPrazoProposta());

        return proposta;
    }

    public PropostaResponse toDTO(Proposta proposta) {
        if (proposta == null) {
            return null;
        }

        return new PropostaResponse(
                proposta.getIdProposta(),
                Optional.ofNullable(proposta.getGrupoPesquisa()).map(grupoPesquisaMapper::toDTO).orElse(null),
                Optional.ofNullable(proposta.getInstituicao()).map(instituicaoMapper::toDTO).orElse(null),
                proposta.getNome(),
                proposta.getSolucao(),
                proposta.getDescricao(),
                proposta.getResumo(),
                proposta.getOrcamento(),
                proposta.getRestricoes(),
                proposta.getRecursosNecessarios(),
                proposta.getStatus(),
                proposta.getDataPrazoProposta(),
                proposta.getDataRegistro()
        );
    }
}
