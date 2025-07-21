package com.marketplace.ifba.service;

import com.marketplace.ifba.dto.InstituicaoRequest;
import com.marketplace.ifba.dto.InstituicaoResponse;
import com.marketplace.ifba.exception.DadoNaoEncontradoException;
import com.marketplace.ifba.mapper.InstituicaoMapper;
import com.marketplace.ifba.model.Instituicao;
import com.marketplace.ifba.model.User;
import com.marketplace.ifba.model.enums.StatusInstituicao;
import com.marketplace.ifba.repository.InstituicaoRepository;
import com.marketplace.ifba.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InstituicaoService {

    private final InstituicaoRepository instituicaoRepository;
    private final UserRepository userRepository;
    private final InstituicaoMapper instituicaoMapper;

    public InstituicaoService(InstituicaoRepository instituicaoRepository, UserRepository userRepository, InstituicaoMapper instituicaoMapper) {
        this.instituicaoRepository = instituicaoRepository;
        this.userRepository = userRepository;
        this.instituicaoMapper = instituicaoMapper;
    }

    // ---------- LEITURA

    // BUSCA INSTITUIÇÃO PELO SEU ID
    @Transactional(readOnly = true)
    public InstituicaoResponse buscarInstituicaoPorId(UUID idInstituicao) {
        return instituicaoMapper.toDTO(instituicaoRepository.findById(idInstituicao)
                .orElseThrow(() -> new DadoNaoEncontradoException("Não foi encontrado usuário com esse ID")));
    }

    // BUSCA INSTITUIÇÃO PELO SEU NOME
    @Transactional(readOnly = true)
    public InstituicaoResponse buscarInstituicaoPorNome(String nome) {
        return instituicaoMapper.toDTO(instituicaoRepository.findAll().stream().filter(inst -> inst.getNome().equals(nome)).findFirst().orElseThrow(() -> new DadoNaoEncontradoException("Não foi encontrado usuário com esse nome")));
    }

    // LISTA TODAS INSTITUIÇÕES
    @Transactional(readOnly = true)
    public List<InstituicaoResponse> buscarTodasInstituicoes() {
        return instituicaoRepository.findAll().stream()
                .map(instituicaoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ---------- ESCRITA

    // REGISTRA INSTITUIÇÃO
    @Transactional
    public InstituicaoResponse registrarInstituicao(InstituicaoRequest request, UUID idUsuarioRegistrador) {
        Instituicao instituicao = instituicaoMapper.toEntity(request);
        instituicao.setDataRegistro(LocalDateTime.now());
        instituicao.setStatus(StatusInstituicao.AGUARDANDO_APROVACAO);
        instituicao.setUsuarioRegistro(userRepository.findById(idUsuarioRegistrador).orElseThrow(() -> new DadoNaoEncontradoException("Não foi encontrado usuário com esse ID: " + idUsuarioRegistrador)));

        return instituicaoMapper.toDTO(instituicaoRepository.save(instituicao));
    }

    // ATUALIZA INSTITUIÇÃO
    @Transactional
    public InstituicaoResponse atualizarInstituicao(InstituicaoRequest request, UUID idInstituicao) {
        Instituicao instituicao = instituicaoRepository.findById(idInstituicao)
                .orElseThrow(() -> new DadoNaoEncontradoException("Não foi encontrado instituição com esse ID: " + idInstituicao));

        instituicaoMapper.updateEntityFromRequest(request, instituicao);

        return instituicaoMapper.toDTO(instituicaoRepository.save(instituicao));
    }

    // APROVA INSTITUIÇÃO
    @Transactional
    public InstituicaoResponse aprovarInstituicao(UUID idInstituicao, UUID idAdmAprovador) {
        Instituicao instituicao = instituicaoRepository.findById(idInstituicao)
                .orElseThrow(() -> new DadoNaoEncontradoException("Instituição não encontrada para aprovação com o ID: " + idInstituicao));

        if (!StatusInstituicao.AGUARDANDO_APROVACAO.equals(instituicao.getStatus())) {
            throw new IllegalStateException("A instituição não está no status 'AGUARDANDO_APROVACAO' para ser aprovada.");
        }

        User admAprovador = userRepository.findById(idAdmAprovador)
                .orElseThrow(() -> new DadoNaoEncontradoException("Administrador não encontrado"));

        instituicao.setStatus(StatusInstituicao.APROVADA);
        instituicao.setDataAprovacao(LocalDateTime.now());
        instituicao.setAdmAprovacao(admAprovador);

        return instituicaoMapper.toDTO(instituicaoRepository.save(instituicao));
    }

    // REPROVA INSTITUIÇÃO
    @Transactional
    public InstituicaoResponse reprovarInstituicao(UUID idInstituicao, UUID idAdmAprovador) {
        Instituicao instituicao = instituicaoRepository.findById(idInstituicao)
                .orElseThrow(() -> new DadoNaoEncontradoException("Instituição não encontrada para aprovação com o ID: " + idInstituicao));

        if (!StatusInstituicao.AGUARDANDO_APROVACAO.equals(instituicao.getStatus())) {
            throw new IllegalStateException("A instituição não está no status 'AGUARDANDO_APROVACAO' para ser aprovada.");
        }

        User admAprovador = userRepository.findById(idAdmAprovador)
                .orElseThrow(() -> new DadoNaoEncontradoException("Administrador não encontrado"));

        instituicao.setStatus(StatusInstituicao.NAO_APROVADA);
        instituicao.setDataAprovacao(LocalDateTime.now());
        instituicao.setAdmAprovacao(admAprovador);

        return instituicaoMapper.toDTO(instituicaoRepository.save(instituicao));
    }

    // REMOVE INSTITUIÇÃO
    @Transactional
    public void removerInstituicao(UUID idInstituicao) {
        if (!instituicaoRepository.existsById(idInstituicao)) {
            throw new DadoNaoEncontradoException("Instituição não encontrada para exclusão com o ID: " + idInstituicao);
        }
        instituicaoRepository.deleteById(idInstituicao);
    }
}
