package com.marketplace.ifba.service;

import com.marketplace.ifba.exception.*;
import com.marketplace.ifba.model.*;
import com.marketplace.ifba.model.enums.StatusGrupoPesquisa;
import com.marketplace.ifba.model.enums.StatusSolicitacao;
import com.marketplace.ifba.repository.GrupoPesquisaRepository;
import com.marketplace.ifba.repository.InstituicaoRepository;
import com.marketplace.ifba.repository.AreaRepository;
import com.marketplace.ifba.repository.UserRepository;
import com.marketplace.ifba.repository.DemandaRepository;
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
    private final DemandaRepository demandaRepository;

    public GrupoPesquisaService(GrupoPesquisaRepository grupoPesquisaRepository,
            InstituicaoRepository instituicaoRepository,
            UserRepository userRepository,
            AreaRepository areaRepository,
            DemandaRepository demandaRepository) {
        this.grupoPesquisaRepository = grupoPesquisaRepository;
        this.instituicaoRepository = instituicaoRepository;
        this.userRepository = userRepository;
        this.areaRepository = areaRepository;
        this.demandaRepository = demandaRepository;
    }

    // ---------- LEITURA

    // BUSCA GRUPO PESQUISA PELO SEU ID
    @Transactional(readOnly = true)
    public GrupoPesquisa buscarGrupoPesquisaPorId(UUID idGrupoPesquisa) {
        return grupoPesquisaRepository.findById(idGrupoPesquisa)
                .orElseThrow(() -> new GrupoPesquisaInvalidoException("Grupo de pesquisa não encontrado com o ID"));
    }

    // BUSCA GRUPO PESQUISA PELO SEU NOME
    @Transactional(readOnly = true)
    public GrupoPesquisa buscarGrupoPesquisaPorNome(String nome) {
        return grupoPesquisaRepository.findAll().stream().filter(grupoPesquisa -> grupoPesquisa.getNome().equals(nome))
                .findFirst()
                .orElseThrow(() -> new GrupoPesquisaInvalidoException("Grupo de pesquisa não encontrado com o NOME"));
    }

    // BUSCA GRUPOS PESQUISA POR INSTITUIÇÃO
    @Transactional(readOnly = true)
    public List<GrupoPesquisa> buscarGruposPorInstituicao(UUID idInstituicao) {
        if (!instituicaoRepository.existsById(idInstituicao)) {
            throw new InstituicaoInvalidaException("Instituição não encontrada com o ID");
        }

        return grupoPesquisaRepository.findAll().stream()
                .filter(grupoPesquisa -> grupoPesquisa.getInstituicao().getIdInstituicao().equals(idInstituicao))
                .toList();
    }

    // BUSCA DADOS DE USUÁRIOS QUE SOLICITARAM A ENTRADA NO GRUPO PESQUISA
    @Transactional(readOnly = true)
    public List<User> buscarUsuariosSolicitantesAssociacao(UUID idOrganizacao) {
        GrupoPesquisa grupoPesquisaSaved = grupoPesquisaRepository.findById(idOrganizacao).orElse(null);

        List<User> usuariosSolicitantes = new ArrayList<>();

        if (grupoPesquisaSaved.getSolicitacoes() != null) {
            for (Solicitacao solicitacao : grupoPesquisaSaved.getSolicitacoes()) {
                if (solicitacao.getStatus().equals(StatusSolicitacao.ATIVA)) {
                    usuariosSolicitantes.add(solicitacao.getUserApplicant());
                }
            }
        }
        return usuariosSolicitantes;
    }

    // LISTA TODOS GRUPOS PESQUISA
    @Transactional(readOnly = true)
    public List<GrupoPesquisa> buscarTodosGruposPesquisa() {
        return grupoPesquisaRepository.findAll();
    }

    // ---------- ESCRITA

    // REGISTRA GRUPO PESQUISA
    @Transactional
    public GrupoPesquisa registrarGrupoPesquisa(GrupoPesquisa grupoPesquisa, UUID idInstituicao,
            UUID idUsuarioRegistrador, List<UUID> idAreas) {
        if (grupoPesquisaRepository.findAll().stream()
                .anyMatch(grupoPes -> grupoPes.getNome().equals(grupoPesquisa.getNome()))) {
            throw new GrupoPesquisaInvalidoException("Já existe um grupo de pesquisa com o NOME");
        }

        grupoPesquisa.setDataRegistro(LocalDateTime.now());
        grupoPesquisa.setStatus(StatusGrupoPesquisa.ATIVO);
        grupoPesquisa.setTrabalhos(0);
        grupoPesquisa.setClassificacao(0.0);
        grupoPesquisa.setSolicitacoes(new ArrayList<>());

        Instituicao instituicao = instituicaoRepository.findById(idInstituicao)
                .orElseThrow(() -> new InstituicaoInvalidaException("Instituição não encontrada com o ID"));
        grupoPesquisa.setInstituicao(instituicao);

        User usuarioResgistrador = userRepository.findById(idUsuarioRegistrador)
                .orElseThrow(() -> new UsuarioInvalidoException("Usuário não encontrada com o ID"));

        if (usuarioResgistrador.getGrupoPesquisa() != null) {
            throw new GrupoPesquisaInvalidoException("Usuário registrador já criou um grupo de pesquisa");
        }
        grupoPesquisa.setUsuarioRegistrador(usuarioResgistrador);
        usuarioResgistrador.setGrupoPesquisa(grupoPesquisa);
        grupoPesquisa.setUsuarios(new ArrayList<>());

        if (grupoPesquisa.getAreas() != null) {
            List<Area> areas = areaRepository.findAllById(idAreas);
            if (areas.size() != idAreas.size()) {
                throw new AreaInvalidaException("Um ou mais IDs de areas fornecidos não foram encontrados.");
            }
            grupoPesquisa.setAreas(new ArrayList<>(areas));
        }
        userRepository.save(usuarioResgistrador);
        return grupoPesquisaRepository.save(grupoPesquisa);
    }

    // ATUALIZA GRUPO PESQUISA
    @Transactional
    public GrupoPesquisa atualizarGrupoPesquisa(UUID idGrupoPesquisa, GrupoPesquisa grupoPesquisa) {
        GrupoPesquisa grupoPesquisaSaved = grupoPesquisaRepository.findById(idGrupoPesquisa)
                .orElseThrow(() -> new GrupoPesquisaInvalidoException(
                        "Grupo de pesquisa não encontrado para atualização com o ID"));

        if (!grupoPesquisaSaved.getNome().equals(grupoPesquisa.getNome())) {
            if (grupoPesquisaRepository.findAll().stream()
                    .anyMatch(grupoPes -> grupoPes.getNome().equals(grupoPesquisa.getNome()))) {
                throw new GrupoPesquisaInvalidoException("Já existe outro grupo de pesquisa com o NOME");
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
                .orElseThrow(() -> new GrupoPesquisaInvalidoException("Grupo de pesquisa não encontrado com o ID"));
        grupo.setStatus(novoStatus);
        return grupoPesquisaRepository.save(grupo);
    }

    // REMOVE GRUPO PESQUISA
    @Transactional
    public void removerGrupoPesquisa(UUID idGrupoPesquisa) {
        if (!grupoPesquisaRepository.existsById(idGrupoPesquisa)) {
            throw new GrupoPesquisaInvalidoException(
                    "Grupo de pesquisa não encontrado para exclusão com o ID: " + idGrupoPesquisa);
        }
        grupoPesquisaRepository.deleteById(idGrupoPesquisa);
    }

    // BUSCA DEMANDAS RECEBIDAS POR UM GRUPO DE PESQUISA
    // @Transactional(readOnly = true)
    // public List<Demanda> buscarDemandasRecebidas(UUID idGrupoPesquisa) {
    // GrupoPesquisa grupoPesquisa =
    // grupoPesquisaRepository.findById(idGrupoPesquisa)
    // .orElseThrow(() -> new GrupoPesquisaInvalidoException("Grupo de pesquisa não
    // encontrado com o ID: " + idGrupoPesquisa));

    // return grupoPesquisa.getDemandas();
    // }

    // BUSCA DEMANDAS RECEBIDAS POR UM GRUPO DE PESQUISA - GARANTE QUE NUNCA RETORNA
    // NULL DIFERENTE DA VERSÃO ACIMA QUE PODE RETORNAR NULL E NOS TESTES QUEBRAR.

    @Transactional(readOnly = true)
    public List<Demanda> buscarDemandasRecebidas(UUID idGrupoPesquisa) {
        GrupoPesquisa grupoPesquisa = grupoPesquisaRepository.findById(idGrupoPesquisa)
                .orElseThrow(() -> new GrupoPesquisaInvalidoException(
                        "Grupo de pesquisa não encontrado com o ID: " + idGrupoPesquisa));

        // Garante que nunca retorna null
        return grupoPesquisa.getDemandas() != null ? grupoPesquisa.getDemandas() : new ArrayList<>();
    }

    // REMOVE DEMANDA DE UM GRUPO DE PESQUISA
    @Transactional
    public void removerDemandaDoGrupo(UUID idGrupoPesquisa, UUID idDemanda) {
        GrupoPesquisa grupoPesquisa = grupoPesquisaRepository.findById(idGrupoPesquisa)
                .orElseThrow(() -> new GrupoPesquisaInvalidoException(
                        "Grupo de pesquisa não encontrado com o ID: " + idGrupoPesquisa));

        Demanda demanda = demandaRepository.findById(idDemanda)
                .orElseThrow(() -> new DemandaInvalidaException("Demanda não encontrada com o ID: " + idDemanda));

        if (grupoPesquisa.getDemandas() == null || !grupoPesquisa.getDemandas().contains(demanda)) {
            throw new DemandaInvalidaException("Demanda não está associada a este grupo de pesquisa");
        }

        grupoPesquisa.removerDemanda(demanda);

        if (demanda.getGruposPesquisa() != null) {
            demanda.getGruposPesquisa().remove(grupoPesquisa);
        }

        grupoPesquisaRepository.save(grupoPesquisa);
        demandaRepository.save(demanda);
    }
}
