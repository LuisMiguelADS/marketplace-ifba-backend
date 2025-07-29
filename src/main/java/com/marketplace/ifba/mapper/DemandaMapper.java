package com.marketplace.ifba.mapper;

import com.marketplace.ifba.dto.DemandaRequest;
import com.marketplace.ifba.dto.DemandaResponse;
import com.marketplace.ifba.model.Demanda;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DemandaMapper {

    private final UserMapper userMapper;
    private final OrganizacaoMapper organizacaoMapper;

    public DemandaMapper(UserMapper userMapper, OrganizacaoMapper organizacaoMapper) {
        this.userMapper = userMapper;
        this.organizacaoMapper = organizacaoMapper;
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
                Optional.ofNullable(demanda.getOrganizacao()).map(organizacaoMapper::toDTO).orElse(null)
        );
    }

    public void updateEntityFromRequest(DemandaRequest request, Demanda demanda) {
        if (request == null || demanda == null) {
            return;
        }

        Optional.ofNullable(request.nome()).ifPresent(demanda::setNome);
        Optional.ofNullable(request.emailResponsavel()).ifPresent(demanda::setEmailResponsavel);
        Optional.ofNullable(request.orcamento()).ifPresent(demanda::setOrcamento);
        Optional.ofNullable(request.descricao()).ifPresent(demanda::setDescricao);
        Optional.ofNullable(request.resumo()).ifPresent(demanda::setResumo);
        Optional.ofNullable(request.criterio()).ifPresent(demanda::setCriterio);
        Optional.ofNullable(request.dataPrazoFinal()).ifPresent(demanda::setDataPrazoFinal);
    }
}
