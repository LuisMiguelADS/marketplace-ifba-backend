package com.marketplace.ifba.service;

import com.marketplace.ifba.dto.OrganizacaoRequest;
import com.marketplace.ifba.dto.OrganizacaoResponse;
import com.marketplace.ifba.exception.DadoConflitoException;
import com.marketplace.ifba.exception.DadoNaoEncontradoException;
import com.marketplace.ifba.mapper.OrganizacaoMapper;
import com.marketplace.ifba.model.Organizacao;
import com.marketplace.ifba.model.User;
import com.marketplace.ifba.model.enums.StatusOrganizacao;
import com.marketplace.ifba.repository.OrganizacaoRepository;
import com.marketplace.ifba.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrganizacaoService {

    private final OrganizacaoRepository organizacaoRepository;
    private final UserRepository userRepository;
    private final OrganizacaoMapper organizacaoMapper;

    public OrganizacaoService(OrganizacaoRepository organizacaoRepository, UserRepository userRepository, OrganizacaoMapper organizacaoMapper) {
        this.organizacaoRepository = organizacaoRepository;
        this.userRepository = userRepository;
        this.organizacaoMapper = organizacaoMapper;
    }

    // ---------- LEITURA

    // BUSCA ORGANIZAÇÃO PELO SEU ID
    @Transactional(readOnly = true)
    public OrganizacaoResponse buscarOrganizacaoPorId(UUID idOrganizacao) {
        Organizacao organizacao = organizacaoRepository.findById(idOrganizacao)
                .orElseThrow(() -> new DadoNaoEncontradoException("Organização não encontrada com o ID: " + idOrganizacao));
        return organizacaoMapper.toDTO(organizacao);
    }

    // LISTA TODAS AS ORGANIZAÇÕES DO SISTEMA
    @Transactional(readOnly = true)
    public List<OrganizacaoResponse> buscarTodasOrganizacoes() {
        return organizacaoRepository.findAll().stream()
                .map(organizacaoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // BUSCA ORGANIZAÇÃO PELO SEU NOME
    @Transactional(readOnly = true)
    public OrganizacaoResponse buscarOrganizacaoPorNome(String nome) {
        Organizacao organizacao = organizacaoRepository.findByNome(nome)
                .orElseThrow(() -> new DadoNaoEncontradoException("Organização não encontrada com o nome: " + nome));
        return organizacaoMapper.toDTO(organizacao);
    }

    // ---------- ESCRITA

    // REGISTRA ORGANIZAÇÃO
    @Transactional
    public OrganizacaoResponse registrarOrganizacao(OrganizacaoRequest request, UUID idUsuarioRegistrador) {
        if (organizacaoRepository.findByNome(request.nome()).isPresent()) {
            throw new DadoConflitoException("Já existe uma organização com o nome: '" + request.nome() + "'.");
        }
        if (organizacaoRepository.findByCnpj(request.cnpj()).isPresent()) {
            throw new DadoConflitoException("Já existe uma organização com o CNPJ: '" + request.cnpj() + "'.");
        }

        Organizacao organizacao = organizacaoMapper.toEntity(request);
        organizacao.setDataRegistro(LocalDateTime.now());
        organizacao.setStatus(StatusOrganizacao.AGUARDANDO_APROVACAO);

        User usuarioRegistro = userRepository.findById(idUsuarioRegistrador)
                .orElseThrow(() -> new DadoNaoEncontradoException("Usuário registrador não encontrado com o ID: " + idUsuarioRegistrador));
        organizacao.setUsuarioRegistro(usuarioRegistro);

        return organizacaoMapper.toDTO(organizacaoRepository.save(organizacao));
    }

    // ATUALIZA ORGANIZAÇÃO
    @Transactional
    public OrganizacaoResponse atualizarOrganizacao(UUID idOrganizacao, OrganizacaoRequest request) {
        Organizacao organizacaoExistente = organizacaoRepository.findById(idOrganizacao)
                .orElseThrow(() -> new DadoNaoEncontradoException("Organização não encontrada para atualização com o ID: " + idOrganizacao));

        if (!organizacaoExistente.getNome().equals(request.nome())) {
            organizacaoRepository.findByNome(request.nome()).ifPresent(org -> {
                if (!org.getIdOrganizacao().equals(idOrganizacao)) {
                    throw new DadoConflitoException("Já existe outra organização com o nome: '" + request.nome() + "'.");
                }
            });
        }

        organizacaoMapper.updateEntityFromRequest(request, organizacaoExistente);

        return organizacaoMapper.toDTO(organizacaoRepository.save(organizacaoExistente));
    }


    // APROVA ORGANIZAÇÃO
    @Transactional
    public OrganizacaoResponse aprovarOrganizacao(UUID idOrganizacao, UUID idAdmAprovador) {
        Organizacao organizacao = organizacaoRepository.findById(idOrganizacao)
                .orElseThrow(() -> new DadoNaoEncontradoException("Organização não encontrada para aprovação com o ID: " + idOrganizacao));

        if (!StatusOrganizacao.AGUARDANDO_APROVACAO.equals(organizacao.getStatus())) {
            throw new IllegalStateException("A organização não está no status 'PENDENTE_APROVACAO' para ser aprovada.");
        }

        User admAprovador = userRepository.findById(idAdmAprovador)
                .orElseThrow(() -> new DadoNaoEncontradoException("Administrador aprovador não encontrado com o ID: " + idAdmAprovador));

        organizacao.setStatus(StatusOrganizacao.APROVADA);
        organizacao.setDataAprovacao(LocalDateTime.now());
        organizacao.setAdmAprovacao(admAprovador);

        return organizacaoMapper.toDTO(organizacaoRepository.save(organizacao));
    }

    // REMOVE ORGANIZAÇÃO
    @Transactional
    public void removerOrganizacao(UUID idOrganizacao) {
        if (!organizacaoRepository.existsById(idOrganizacao)) {
            throw new DadoNaoEncontradoException("Organização não encontrada para exclusão com o ID: " + idOrganizacao);
        }
        organizacaoRepository.deleteById(idOrganizacao);
    }
}
