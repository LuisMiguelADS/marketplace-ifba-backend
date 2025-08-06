package com.marketplace.ifba.mapper;

import com.marketplace.ifba.dto.OrganizacaoRequest;
import com.marketplace.ifba.dto.OrganizacaoResponse;
import com.marketplace.ifba.dto.SolicitacaoResponse;
import com.marketplace.ifba.dto.UserResponse;
import com.marketplace.ifba.model.Organizacao;
import com.marketplace.ifba.model.Solicitacao;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class OrganizacaoMapper {

    private final UserMapper userMapper;
    private final SolicitacaoMapper solicitacaoMapper;

    public OrganizacaoMapper(UserMapper userMapper, SolicitacaoMapper solicitacaoMapper) {
        this.userMapper = userMapper;
        this.solicitacaoMapper = solicitacaoMapper;
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

        UserResponse usuarioGerente = Optional.ofNullable(organizacao.getUsuarioGerente())
                .map(userMapper::toDTO)
                .orElse(null);

        List<UserResponse> usuariosIntegrantes = organizacao.getUsuariosIntegrantes().stream()
                .map(userMapper::toDTO)
                .toList();

        List<SolicitacaoResponse> solicitacoes = new ArrayList<>();

        if (organizacao.getSolicitacoes() != null) {
            for (Solicitacao solicitacao : organizacao.getSolicitacoes()) {
                solicitacoes.add(solicitacaoMapper.toDTO(solicitacao));
            }
        }

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
                organizacao.getDescricao(),
                organizacao.getDataRegistro(),
                organizacao.getDataAprovacao(),
                admAprovacaoDTO,
                usuarioRegistroDTO,
                usuarioGerente,
                usuariosIntegrantes,
                solicitacoes
        );
    }
}
