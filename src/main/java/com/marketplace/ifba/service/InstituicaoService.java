package com.marketplace.ifba.service;

import com.marketplace.ifba.dto.InstituicaoRequest;
import com.marketplace.ifba.dto.InstituicaoResponse;
import com.marketplace.ifba.exception.DadoNaoEncontradoException;
import com.marketplace.ifba.mapper.InstituicaoMapper;
import com.marketplace.ifba.model.Instituicao;
import com.marketplace.ifba.model.User;
import com.marketplace.ifba.repository.InstituicaoRepository;
import com.marketplace.ifba.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InstituicaoService {

    @Autowired
    private final InstituicaoRepository instituicaoRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final InstituicaoMapper instituicaoMapper;

    public InstituicaoService(InstituicaoRepository instituicaoRepository, UserRepository userRepository, InstituicaoMapper instituicaoMapper) {
        this.instituicaoRepository = instituicaoRepository;
        this.userRepository = userRepository;
        this.instituicaoMapper = instituicaoMapper;
    }

    // Leitura

    @Transactional(readOnly = true)
    public InstituicaoResponse buscarInstituicaoPorId(UUID idInstituicao) {
        return instituicaoMapper.toDTO(instituicaoRepository.findById(idInstituicao).orElseThrow(() -> new DadoNaoEncontradoException("Não foi encontrado usuário com esse ID")));
    }

    @Transactional(readOnly = true)
    public List<InstituicaoResponse> buscarTodasInstituicoes() {
        return instituicaoRepository.findAll().stream()
                .map(instituicaoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InstituicaoResponse buscarInstituicaoPorNome(String nome) {
        return instituicaoMapper.toDTO(instituicaoRepository.findAll().stream().filter(inst -> inst.getNome().equals(nome)).findFirst().orElseThrow(() -> new DadoNaoEncontradoException("Não foi encontrado usuário com esse nome")));
    }

    // Escrita

    @Transactional
    public InstituicaoResponse registrarInstituicao(InstituicaoRequest request, UUID idUsuarioRegistrador) {
        Instituicao instituicao = instituicaoMapper.toEntity(request);
        instituicao.setDataRegistro(LocalDateTime.now());
        instituicao.setStatus("PENDENTE_APROVACAO"); // Trocar para ENUM depois
        instituicao.setUsuarioRegistro(userRepository.findById(idUsuarioRegistrador).orElseThrow(() -> new DadoNaoEncontradoException("Não foi encontrado usuário com esse ID: " + idUsuarioRegistrador)));

        return instituicaoMapper.toDTO(instituicaoRepository.save(instituicao));
    }

    @Transactional
    public InstituicaoResponse atualizarInstituicao(InstituicaoRequest request, UUID idInstituicao) {
        Instituicao instituicao = instituicaoRepository.findById(idInstituicao)
                .orElseThrow(() -> new DadoNaoEncontradoException("Não foi encontrado instituição com esse ID: " + idInstituicao));

        instituicao.setNome(request.nome());
        instituicao.setSigla(request.sigla());
        instituicao.setCnpj(request.cnpj());
        instituicao.setTipoInstituicao(request.tipoInstituicao());
        instituicao.setSetor(request.setor());
        instituicao.setTelefone(request.telefone());
        instituicao.setSite(request.site());
        instituicao.setLogoURL(request.logoURL());
        instituicao.setDescricao(request.descricao());
        instituicao.setDataAtualizacao(LocalDateTime.now());

        return instituicaoMapper.toDTO(instituicaoRepository.save(instituicao));
    }

    @Transactional
    public InstituicaoResponse aprovarInstituicao(UUID idInstituicao, UUID idAdmAprovador) {
        Instituicao instituicao = instituicaoRepository.findById(idInstituicao)
                .orElseThrow(() -> new DadoNaoEncontradoException("Instituição não encontrada para aprovação com o ID: " + idInstituicao));

        if (!"PENDENTE_APROVACAO".equals(instituicao.getStatus())) {
            throw new IllegalStateException("A instituição não está no status 'PENDENTE_APROVACAO' para ser aprovada.");
        }

        User admAprovador = userRepository.findById(idAdmAprovador)
                .orElseThrow(() -> new DadoNaoEncontradoException("Administrador não encontrado"));

        instituicao.setStatus("APROVADA"); // Alterar para ENUM
        instituicao.setDataAprovacao(LocalDateTime.now());
        instituicao.setAdmAprovacao(admAprovador);

        return instituicaoMapper.toDTO(instituicaoRepository.save(instituicao));
    }

    @Transactional
    public void deletarInstituicao(UUID idInstituicao) {
        if (!instituicaoRepository.existsById(idInstituicao)) {
            throw new DadoNaoEncontradoException("Instituição não encontrada para exclusão com o ID: " + idInstituicao);
        }
        instituicaoRepository.deleteById(idInstituicao);
    }
}
