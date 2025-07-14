package com.marketplace.ifba.mapper;

import com.marketplace.ifba.dto.InstituicaoRequest;
import com.marketplace.ifba.dto.InstituicaoResponse;
import com.marketplace.ifba.model.Instituicao;
import org.springframework.stereotype.Component;

@Component
public class InstituicaoMapper {

    public Instituicao toEntity(InstituicaoRequest request) {
        if (request == null) {
            return null;
        }

        Instituicao instituicao = new Instituicao();
        instituicao.setNome(request.nome());
        instituicao.setSigla(request.sigla());
        instituicao.setCnpj(request.cnpj());
        instituicao.setTipoInstituicao(request.tipoInstituicao());
        instituicao.setSetor(request.setor());
        instituicao.setTelefone(request.telefone());
        instituicao.setSite(request.site());
        instituicao.setLogoURL(request.logoURL());
        instituicao.setDescricao(request.descricao());

        return instituicao;
    }

    public InstituicaoResponse toDTO(Instituicao instituicao) {
        if (instituicao == null) {
            return null;
        }

        String nomeAdmAprovacao = null;
        if (instituicao.getAdmAprovacao() != null) {
            nomeAdmAprovacao = instituicao.getAdmAprovacao().getNomeCompleto();
        }

        String nomeUsuarioRegistro = null;
        if (instituicao.getUsuarioRegistro() != null) {
            nomeUsuarioRegistro = instituicao.getUsuarioRegistro().getNomeCompleto();
        }

        return new InstituicaoResponse(
                instituicao.getIdInstituicao(),
                instituicao.getNome(),
                instituicao.getSigla(),
                instituicao.getCnpj(),
                instituicao.getTipoInstituicao(),
                instituicao.getSetor(),
                instituicao.getTelefone(),
                instituicao.getSite(),
                instituicao.getStatus(),
                instituicao.getLogoURL(),
                instituicao.getDescricao(),
                instituicao.getDataRegistro(),
                instituicao.getDataAprovacao(),
                instituicao.getDataAtualizacao(),
                nomeAdmAprovacao,
                nomeUsuarioRegistro
        );
    }
}
