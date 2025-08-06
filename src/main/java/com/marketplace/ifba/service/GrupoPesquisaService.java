package com.marketplace.ifba.service;

import com.marketplace.ifba.exception.DadoConflitoException;
import com.marketplace.ifba.exception.DadoNaoEncontradoException;
import com.marketplace.ifba.model.GrupoPesquisa;
import com.marketplace.ifba.model.Instituicao;
import com.marketplace.ifba.model.Area;
import com.marketplace.ifba.model.User;
import com.marketplace.ifba.model.enums.StatusGrupoPesquisa;
import com.marketplace.ifba.repository.GrupoPesquisaRepository;
import com.marketplace.ifba.repository.InstituicaoRepository;
import com.marketplace.ifba.repository.AreaRepository;
import com.marketplace.ifba.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GrupoPesquisaService {

    private final GrupoPesquisaRepository grupoPesquisaRepository;
    private final InstituicaoRepository instituicaoRepository;
    private final UserRepository userRepository;
    private final AreaRepository areaRepository;

    public GrupoPesquisaService(GrupoPesquisaRepository grupoPesquisaRepository,
                                InstituicaoRepository instituicaoRepository,
                                UserRepository userRepository,
                                AreaRepository areaRepository) {
        this.grupoPesquisaRepository = grupoPesquisaRepository;
        this.instituicaoRepository = instituicaoRepository;
        this.userRepository = userRepository;
        this.areaRepository = areaRepository;
    }

    // ---------- LEITURA

    // BUSCA GRUPO PESQUISA PELO SEU ID
    @Transactional(readOnly = true)
    public GrupoPesquisa buscarGrupoPesquisaPorId(UUID idGrupoPesquisa) {
        return grupoPesquisaRepository.findById(idGrupoPesquisa)
                .orElseThrow(() -> new DadoNaoEncontradoException("Grupo de pesquisa não encontrado com o ID"));
    }

    // BUSCA GRUPO PESQUISA PELO SEU NOME
    @Transactional(readOnly = true)
    public GrupoPesquisa buscarGrupoPesquisaPorNome(String nome) {
        return grupoPesquisaRepository.findAll().stream().filter(grupoPesquisa -> grupoPesquisa.getNome().equals(nome)).findFirst()
                .orElseThrow(() -> new DadoNaoEncontradoException("Grupo de pesquisa não encontrado com o NOME"));
    }

    // BUSCA GRUPOS PESQUISA POR INSTITUIÇÃO
    @Transactional(readOnly = true)
    public List<GrupoPesquisa> buscarGruposPorInstituicao(UUID idInstituicao) {
        if (!instituicaoRepository.existsById(idInstituicao)) {
            throw new DadoNaoEncontradoException("Instituição não encontrada com o ID");
        }

        return grupoPesquisaRepository.findAll().stream()
                .filter(grupoPesquisa -> grupoPesquisa.getInstituicao().getIdInstituicao().equals(idInstituicao))
                .toList();
    }

    // LISTA TODOS GRUPOS PESQUISA
    @Transactional(readOnly = true)
    public List<GrupoPesquisa> buscarTodosGruposPesquisa() {
        return grupoPesquisaRepository.findAll();
    }

    // ---------- ESCRITA

    // REGISTRA GRUPO PESQUISA
    @Transactional
    public GrupoPesquisa registrarGrupoPesquisa(GrupoPesquisa grupoPesquisa, UUID idInstituicao, UUID idUsuarioRegistrador, List<UUID> idAreas) {
        if (grupoPesquisaRepository.findAll().stream().anyMatch(grupoPes -> grupoPes.getNome().equals(grupoPesquisa.getNome()))) {
            throw new DadoConflitoException("Já existe um grupo de pesquisa com o NOME");
        }

        grupoPesquisa.setDataRegistro(LocalDateTime.now());
        grupoPesquisa.setStatus(StatusGrupoPesquisa.ATIVO);
        grupoPesquisa.setTrabalhos(0);
        grupoPesquisa.setClassificacao(0.0);

        Instituicao instituicao = instituicaoRepository.findById(idInstituicao)
                .orElseThrow(() -> new DadoNaoEncontradoException("Instituição não encontrada com o ID"));
        grupoPesquisa.setInstituicao(instituicao);

        User usuarioResgistrador = userRepository.findById(idUsuarioRegistrador)
                .orElseThrow(() -> new DadoNaoEncontradoException("Usuário não encontrada com o ID"));
        grupoPesquisa.setUsuarioRegistrador(usuarioResgistrador);

        if (grupoPesquisa.getAreas() != null) {
            List<Area> areas = areaRepository.findAllById(idAreas);
            if (areas.size() != idAreas.size()) {
                throw new DadoNaoEncontradoException("Um ou mais IDs de tags fornecidos não foram encontrados.");
            }
            grupoPesquisa.setAreas(new ArrayList<>(areas));
        }
        return grupoPesquisaRepository.save(grupoPesquisa);
    }

    // ATUALIZA GRUPO PESQUISA
    @Transactional
    public GrupoPesquisa atualizarGrupoPesquisa(UUID idGrupoPesquisa, GrupoPesquisa grupoPesquisa) {
        GrupoPesquisa grupoPesquisaSaved = grupoPesquisaRepository.findById(idGrupoPesquisa)
                .orElseThrow(() -> new DadoNaoEncontradoException("Grupo de pesquisa não encontrado para atualização com o ID"));

        if (!grupoPesquisaSaved.getNome().equals(grupoPesquisa.getNome())) {
            if (grupoPesquisaRepository.findAll().stream().anyMatch(grupoPes -> grupoPes.getNome().equals(grupoPesquisa.getNome()))) {
                throw new DadoConflitoException("Já existe outro grupo de pesquisa com o NOME");
            }
        }

        // ATRIBUTOS QUE PODEM SER ALTERADOS
        grupoPesquisaSaved.setNome(grupoPesquisa.getNome());
        grupoPesquisaSaved.setAreas(grupoPesquisa.getAreas());
        grupoPesquisaSaved.setDescricao(grupoPesquisa.getDescricao());

        return grupoPesquisaRepository.save(grupoPesquisaSaved);
    }

    // ATUALIZA STATUS GRUPO PESQUISA
    @Transactional
    public GrupoPesquisa atualizarStatusGrupoPesquisa(UUID idGrupoPesquisa, StatusGrupoPesquisa novoStatus) {
        GrupoPesquisa grupo = grupoPesquisaRepository.findById(idGrupoPesquisa)
                .orElseThrow(() -> new DadoNaoEncontradoException("Grupo de pesquisa não encontrado com o ID"));
        grupo.setStatus(novoStatus);
        return grupoPesquisaRepository.save(grupo);
    }

    // REMOVE GRUPO PESQUISA
    @Transactional
    public void removerGrupoPesquisa(UUID idGrupoPesquisa) {
        if (!grupoPesquisaRepository.existsById(idGrupoPesquisa)) {
            throw new DadoNaoEncontradoException("Grupo de pesquisa não encontrado para exclusão com o ID: " + idGrupoPesquisa);
        }
        grupoPesquisaRepository.deleteById(idGrupoPesquisa);
    }
}
