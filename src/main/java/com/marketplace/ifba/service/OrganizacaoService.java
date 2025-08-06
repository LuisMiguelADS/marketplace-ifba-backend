package com.marketplace.ifba.service;

import com.marketplace.ifba.dto.OrganizacaoRequest;
import com.marketplace.ifba.dto.OrganizacaoResponse;
import com.marketplace.ifba.dto.UserInfosMinResponse;
import com.marketplace.ifba.exception.*;
import com.marketplace.ifba.mapper.OrganizacaoMapper;
import com.marketplace.ifba.model.Organizacao;
import com.marketplace.ifba.model.Solicitacao;
import com.marketplace.ifba.model.User;
import com.marketplace.ifba.model.enums.StatusInstituicao;
import com.marketplace.ifba.model.enums.StatusOrganizacao;
import com.marketplace.ifba.model.enums.StatusSolicitacao;
import com.marketplace.ifba.repository.OrganizacaoRepository;
import com.marketplace.ifba.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrganizacaoService {

    private final OrganizacaoRepository organizacaoRepository;
    private final UserRepository userRepository;

    public OrganizacaoService(OrganizacaoRepository organizacaoRepository, UserRepository userRepository) {
        this.organizacaoRepository = organizacaoRepository;
        this.userRepository = userRepository;
    }

    // ---------- LEITURA

    // BUSCA ORGANIZAÇÃO PELO SEU ID
    @Transactional(readOnly = true)
    public Organizacao buscarOrganizacaoPorId(UUID idOrganizacao) {
        return organizacaoRepository.findById(idOrganizacao)
                .orElseThrow(() -> new OrganizacaoInvalidaException("Organização não encontrada com o ID"));
    }

    // LISTA TODAS AS ORGANIZAÇÕES DO SISTEMA
    @Transactional(readOnly = true)
    public List<Organizacao> buscarTodasOrganizacoes() {
        return organizacaoRepository.findAll();
    }

    // BUSCA ORGANIZAÇÃO PELO SEU NOME
    @Transactional(readOnly = true)
    public Organizacao buscarOrganizacaoPorNome(String nome) {
        return organizacaoRepository.findAll().stream()
                .filter(org -> org.getNome().equals(nome))
                .findFirst()
                .orElseThrow(() -> new OrganizacaoInvalidaException("Organização não encontrada com o nome"));
    }

    // BUSCA ORGANIZAÇÃO PELO SEU CNPJ
    @Transactional(readOnly = true)
    public Organizacao buscarOrganizacaoPorCnpj(String cnpj) {
        return organizacaoRepository.findAll().stream()
                .filter(org -> org.getCnpj().equals(cnpj))
                .findFirst()
                .orElseThrow(() -> new OrganizacaoInvalidaException("Organização não encontrada com o CNPJ"));
    }

    // BUSCA DADOS DE USUÁRIOS QUE SOLICITARAM A ENTRADA NA ORGANIZAÇÃO
    @Transactional(readOnly = true)
    public List<User> buscarUsuariosSolicitantesAssociacao(UUID idOrganizacao) {
        Organizacao organizacaoSaved = organizacaoRepository.findById(idOrganizacao).orElse(null);

        List<User> usuariosSolicitantes = new ArrayList<>();

        if (organizacaoSaved.getSolicitacoes() != null) {
            for (Solicitacao solicitacao : organizacaoSaved.getSolicitacoes()) {
                if (solicitacao.getStatus().equals(StatusSolicitacao.ATIVA)) {
                    usuariosSolicitantes.add(solicitacao.getUserApplicant());
                }
            }
        }
        return usuariosSolicitantes;
    }

    // ---------- ESCRITA

    // REGISTRA ORGANIZAÇÃO
    @Transactional
    public Organizacao registrarOrganizacao(Organizacao organizacao, UUID idUsuarioRegistrador) {
        if (organizacaoRepository.findAll().stream().anyMatch(org -> org.getNome().equals(organizacao.getNome()))) {
            throw new OrganizacaoInvalidaException("Já existe uma organização com o nome");
        }

        if (organizacaoRepository.findAll().stream().anyMatch(org -> org.getCnpj().equals(organizacao.getCnpj()))) {
            throw new OrganizacaoInvalidaException("Já existe uma organização com o CNPJ");
        }

        organizacao.setDataRegistro(LocalDateTime.now());
        organizacao.setStatus(StatusOrganizacao.AGUARDANDO_APROVACAO);

        User usuarioRegistro = userRepository.findById(idUsuarioRegistrador)
                .orElseThrow(() -> new UsuarioInvalidoException("Usuário registrador não encontrado com o ID"));

        if (usuarioRegistro.getOrganizacao() != null) {
            throw new UsuarioInvalidoException("Usuário já está associado a uma organização, não pode solicitar a criação de outra organização");
        }

        if (usuarioRegistro.getInstituicao() != null) {
            throw new UsuarioInvalidoException("Usuário já está associado a uma instituição, não pode solicitar a criação de uma organização");
        }

        organizacao.setUsuarioRegistro(usuarioRegistro);
        organizacao.setUsuarioGerente(usuarioRegistro);
        organizacao.setUsuariosIntegrantes(List.of(usuarioRegistro));

        usuarioRegistro.setOrganizacao(organizacao);
        userRepository.save(usuarioRegistro);

        return organizacaoRepository.save(organizacao);
    }

    // ATUALIZA ORGANIZAÇÃO
    @Transactional
    public Organizacao atualizarOrganizacao(Organizacao organizacao, UUID idOrganizacao) {
        Organizacao organizacaoSaved = organizacaoRepository.findById(idOrganizacao)
                .orElseThrow(() -> new OrganizacaoInvalidaException("Organização não encontrada para atualização com o ID"));

        if (organizacaoSaved.getNome().equals(organizacao.getNome())) {
            throw new OrganizacaoInvalidaException("Já existe outra organização com o nome: '" + organizacao.getNome());
        }

        // ATRIBUTOS QUE PODEM SER ALTERADOS
        organizacaoSaved.setNome(organizacao.getNome());
        organizacaoSaved.setSigla(organizacao.getSigla());
        organizacaoSaved.setTipoOrganizacao(organizacao.getTipoOrganizacao());
        organizacaoSaved.setSetor(organizacao.getSetor());
        organizacaoSaved.setTelefone(organizacao.getTelefone());
        organizacaoSaved.setSite(organizacao.getSite());
        organizacaoSaved.setDescricao(organizacao.getDescricao());

        return organizacaoRepository.save(organizacaoSaved);
    }


    // APROVA OU REPROVA ORGANIZAÇÃO
    @Transactional
    public Organizacao aprovaOuReprovaOrganizacao(UUID idOrganizacao, UUID idAdm, Boolean decisaoAdm) {
        Organizacao organizacaoSaved = organizacaoRepository.findById(idOrganizacao)
                .orElseThrow(() -> new OrganizacaoInvalidaException("Organização não encontrada!"));

        if (!StatusOrganizacao.AGUARDANDO_APROVACAO.equals(organizacaoSaved.getStatus())) {
            throw new IllegalStateException("A organização não está no status 'AGUARDANDO_APROVACAO' para ser aprovada.");
        }

        User adm = userRepository.findById(idAdm)
                .orElseThrow(() -> new AdmException("Administrador aprovador não encontrado!"));

        if (decisaoAdm) {
            organizacaoSaved.setStatus(StatusOrganizacao.APROVADA);
        } else {
            organizacaoSaved.setStatus(StatusOrganizacao.NAO_APROVADA);
        }
        organizacaoSaved.setDataAprovacao(LocalDateTime.now());
        organizacaoSaved.setAdmAprovacao(adm);

        return organizacaoRepository.save(organizacaoSaved);
    }

    // REMOVE ORGANIZAÇÃO
    @Transactional
    public void removerOrganizacao(UUID idOrganizacao) {
        if (!organizacaoRepository.existsById(idOrganizacao)) {
            throw new OrganizacaoInvalidaException("Organização não encontrada para exclusão com o ID");
        }
        organizacaoRepository.deleteById(idOrganizacao);
    }
}
