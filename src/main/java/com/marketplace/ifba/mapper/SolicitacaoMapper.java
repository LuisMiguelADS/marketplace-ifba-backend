package com.marketplace.ifba.mapper;

import com.marketplace.ifba.dto.SolicitacaoResponse;
import com.marketplace.ifba.model.Solicitacao;
import org.springframework.stereotype.Component;

@Component
public class SolicitacaoMapper {

    public SolicitacaoResponse toDTO(Solicitacao solicitacao) {
        if (solicitacao == null) {
            return null;
        }

        return new SolicitacaoResponse(
                solicitacao.getIdSolicitacao(),
                solicitacao.getUserApplicant().getIdUsuario(),
                solicitacao.getOrganizacaoRequested().getIdOrganizacao(),
                solicitacao.getStatus()
        );
    }
}
