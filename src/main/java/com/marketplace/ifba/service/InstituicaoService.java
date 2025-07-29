package com.marketplace.ifba.service;

import com.marketplace.ifba.exception.DadoNaoEncontradoException;
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

@Service
public class InstituicaoService {

    private final InstituicaoRepository instituicaoRepository;
    private final UserRepository userRepository;

    public InstituicaoService(InstituicaoRepository instituicaoRepository, UserRepository userRepository) {
        this.instituicaoRepository = instituicaoRepository;
        this.userRepository = userRepository;
    }

    // ---------- LEITURA

    // BUSCA INSTITUIÇÃO PELO SEU ID
    @Transactional(readOnly = true)
    public Instituicao buscarInstituicaoPorId(UUID idInstituicao) {
        return instituicaoRepository.findById(idInstituicao)
                .orElseThrow(() -> new DadoNaoEncontradoException("Não foi encontrado usuário com esse ID"));
    }

    // BUSCA INSTITUIÇÃO PELO SEU NOME
    @Transactional(readOnly = true)
    public Instituicao buscarInstituicaoPorNome(String nome) {
        return instituicaoRepository.findAll().stream().filter(inst -> inst.getNome().equals(nome)).findFirst().orElseThrow(() -> new DadoNaoEncontradoException("Instituição não encontrada com o nome: " + nome));
    }

    // LISTA TODAS INSTITUIÇÕES
    @Transactional(readOnly = true)
    public List<Instituicao> buscarTodasInstituicoes() {
        return instituicaoRepository.findAll();
    }

    // ---------- ESCRITA

    // REGISTRA INSTITUIÇÃO
    @Transactional
    public Instituicao registrarInstituicao(Instituicao instituicao, UUID idUsuarioRegistrador) {
        instituicao.setDataRegistro(LocalDateTime.now());
        instituicao.setStatus(StatusInstituicao.AGUARDANDO_APROVACAO);
        User user = userRepository.findById(idUsuarioRegistrador)
                .orElseThrow(() -> new DadoNaoEncontradoException("Usuário não encontrado com o ID: " + idUsuarioRegistrador));
        instituicao.setUsuarioRegistro(user);

        return instituicaoRepository.save(instituicao);
    }

    // ATUALIZA INSTITUIÇÃO
    @Transactional
    public Instituicao atualizarInstituicao(Instituicao instituicao, UUID idInstituicao) {
        Instituicao instituicaoSaved = instituicaoRepository.findById(idInstituicao)
                .orElseThrow(() -> new DadoNaoEncontradoException("Não foi encontrado instituição com esse ID: " + idInstituicao));

        // ATRIBUTOS QUE PODEM SER ALTERADOS
        instituicaoSaved.setNome(instituicao.getNome());
        instituicaoSaved.setSigla(instituicao.getSigla());
        instituicaoSaved.setTipoInstituicao(instituicao.getTipoInstituicao());
        instituicaoSaved.setSetor(instituicao.getSetor());
        instituicaoSaved.setTelefone(instituicao.getTelefone());
        instituicaoSaved.setSite(instituicao.getSite());

        return instituicaoRepository.save(instituicaoSaved);
    }

    // APROVA OU REPROVA INSTITUIÇÃO
    @Transactional
    public Instituicao aprovarOuReprovaInstituicao(UUID idInstituicao, UUID idAdm, Boolean decisaoAdm) {
        Instituicao instituicaoSaved = instituicaoRepository.findById(idInstituicao)
                .orElseThrow(() -> new DadoNaoEncontradoException("Instituição não encontrada para aprovação com o ID: " + idInstituicao));

        if (!StatusInstituicao.AGUARDANDO_APROVACAO.equals(instituicaoSaved.getStatus())) {
            throw new IllegalStateException("A instituição não está no status 'AGUARDANDO_APROVACAO' para ser aprovada.");
        }

        User adm = userRepository.findById(idAdm)
                .orElseThrow(() -> new DadoNaoEncontradoException("Administrador não encontrado"));

        if (decisaoAdm) {
            instituicaoSaved.setStatus(StatusInstituicao.APROVADA);
        } else {
            instituicaoSaved.setStatus(StatusInstituicao.NAO_APROVADA);
        }
        instituicaoSaved.setDataAprovacao(LocalDateTime.now());
        instituicaoSaved.setAdmAprovacao(adm);

        return instituicaoRepository.save(instituicaoSaved);
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
