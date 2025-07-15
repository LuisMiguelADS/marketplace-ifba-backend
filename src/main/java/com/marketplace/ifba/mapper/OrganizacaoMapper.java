package com.marketplace.ifba.mapper;

import com.marketplace.ifba.dto.OrganizacaoRequest;
import com.marketplace.ifba.dto.OrganizacaoResponse;
import com.marketplace.ifba.dto.UserResponse;
import com.marketplace.ifba.model.Organizacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrganizacaoMapper {

    @Autowired
    private final UserMapper userMapper;

    public OrganizacaoMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Organizacao toEntity(OrganizacaoRequest request) {
        if (request == null) {
            return null;
        }

        Organizacao organizacao = new Organizacao();
        organizacao.setNome(request.nome());
        organizacao.setSigla(request.sigla());
        organizacao.setCnpj(request.cnpj());
        organizacao.setTipoOrganizacao(request.tipoOrganizacao());
        organizacao.setSetor(request.setor());
        organizacao.setTelefone(request.telefone());
        organizacao.setSite(request.site());
        organizacao.setLogoURL(request.logoURL());
        organizacao.setDescricao(request.descricao());

        return organizacao;
    }

    public OrganizacaoResponse toDTO(Organizacao organizacao) {
        if (organizacao == null) {
            return null;
        }

        UserResponse admAprovacaoDTO = Optional.ofNullable(organizacao.getAdmAprovacao())
                .map(userMapper::toDTO)
                .orElse(null);

        UserResponse usuarioRegistroDTO = Optional.ofNullable(organizacao.getUsuarioRegistro())
                .map(userMapper::toDTO)
                .orElse(null);

        return new OrganizacaoResponse(
                organizacao.getIdOrganizacao(),
                organizacao.getNome(),
                organizacao.getSigla(),
                organizacao.getCnpj(),
                organizacao.getTipoOrganizacao(),
                organizacao.getSetor(),
                organizacao.getTelefone(),
                organizacao.getSite(),
                organizacao.getStatus(),
                organizacao.getLogoURL(),
                organizacao.getDescricao(),
                organizacao.getDataRegistro(),
                organizacao.getDataAprovacao(),
                admAprovacaoDTO,
                usuarioRegistroDTO
        );
    }

    public void updateEntityFromRequest(OrganizacaoRequest request, Organizacao organizacao) {
        if (request == null || organizacao == null) {
            return;
        }

        Optional.ofNullable(request.nome()).ifPresent(organizacao::setNome);
        Optional.ofNullable(request.sigla()).ifPresent(organizacao::setSigla);
        Optional.ofNullable(request.cnpj()).ifPresent(organizacao::setCnpj);
        Optional.ofNullable(request.tipoOrganizacao()).ifPresent(organizacao::setTipoOrganizacao);
        Optional.ofNullable(request.setor()).ifPresent(organizacao::setSetor);
        Optional.ofNullable(request.telefone()).ifPresent(organizacao::setTelefone);
        Optional.ofNullable(request.site()).ifPresent(organizacao::setSite);
        Optional.ofNullable(request.logoURL()).ifPresent(organizacao::setLogoURL);
        Optional.ofNullable(request.descricao()).ifPresent(organizacao::setDescricao);
    }
}
