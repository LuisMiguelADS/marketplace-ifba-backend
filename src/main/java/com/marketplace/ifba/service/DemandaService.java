package com.marketplace.ifba.service;

import com.marketplace.ifba.exception.*;
import com.marketplace.ifba.model.Demanda;
import com.marketplace.ifba.model.GrupoPesquisa;
import com.marketplace.ifba.model.Organizacao;
import com.marketplace.ifba.model.User;
import com.marketplace.ifba.model.enums.StatusDemanda;
import com.marketplace.ifba.repository.DemandaRepository;
import com.marketplace.ifba.repository.GrupoPesquisaRepository;
import com.marketplace.ifba.repository.OrganizacaoRepository;
import com.marketplace.ifba.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class DemandaService {

    private final DemandaRepository demandaRepository;
    private final UserRepository userRepository;
    private final OrganizacaoRepository organizacaoRepository;
    private final GrupoPesquisaRepository grupoPesquisaRepository;

    public DemandaService(DemandaRepository demandaRepository, UserRepository userRepository,
                          OrganizacaoRepository organizacaoRepository, GrupoPesquisaRepository grupoPesquisaRepository) {
        this.demandaRepository = demandaRepository;
        this.userRepository = userRepository;
        this.organizacaoRepository = organizacaoRepository;
        this.grupoPesquisaRepository = grupoPesquisaRepository;
    }

    // ---------- LEITURA

    // BUSCA DEMANDA PELO SEU ID
    @Transactional(readOnly = true)
    public Demanda buscarDemandaPorId(UUID idDemanda) {
        return demandaRepository.findById(idDemanda)
                .orElseThrow(() -> new DemandaInvalidaException("Demanda não encontrada com o ID: " + idDemanda));
    }

    // BUSCA DEMANDA POR NOME
    @Transactional(readOnly = true)
    public Demanda buscarDemandaPorNome(String nome) {
        return demandaRepository.findAll().stream().filter(dem -> dem.getNome().equals(nome)).findFirst().orElseThrow(() -> new DemandaInvalidaException("Demanda não encontrada com o nome: " + nome));
    }

    // REALIZA A ADIÇÃO DE VISUALIZAÇÕES
    @Transactional
    public void incrementarVizualizacao(UUID idDemanda) {
        Demanda demanda = demandaRepository.findById(idDemanda)
                .orElseThrow(() -> new DemandaInvalidaException("Demanda não encontrada com o ID: " + idDemanda));
        demanda.setVisualizacoes(demanda.getVisualizacoes() + 1);
        demandaRepository.save(demanda);
    }

    // LISTA DEMANDAS PELA ORGANIZAÇÃO
    @Transactional(readOnly = true)
    public List<Demanda> buscarDemandasPorOrganizacao(UUID idOrganizacao) {
        if (!organizacaoRepository.existsById(idOrganizacao)) {
            throw new OrganizacaoInvalidaException("Organização não encontrada com o ID: " + idOrganizacao);
        }
        return demandaRepository.findAll().stream()
                .filter(demanda -> demanda.getOrganizacao().getIdOrganizacao().equals(idOrganizacao))
                .toList();
    }

    // LISTA DEMANDAS APROVADAS PELA ORGANIZAÇÃO
    @Transactional(readOnly = true)
    public List<Demanda> buscarDemandasAprovadas() {
        return demandaRepository.findAll().stream()
                .filter(demanda -> demanda.getStatus() == StatusDemanda.AGUARDANDO_PROPOSTA)
                .toList();
    }

    // LISTA TODAS DEMANDAS
    @Transactional(readOnly = true)
    public List<Demanda> buscarTodasDemandas() {
        return demandaRepository.findAll();
    }

    // ---------- ESCRITA

    // REGITRA DEMANDA
    @Transactional
    public Demanda registrarDemanda(Demanda demanda, UUID usuarioRegistrador, UUID idOrganizacao) {
        demanda.setStatus(StatusDemanda.AGUARDANDO_APROVACAO);
        demanda.setVisualizacoes(0);

        User usuarioCriador = userRepository.findById(usuarioRegistrador)
                .orElseThrow(() -> new UsuarioInvalidoException("Usuário criador não encontrado com o ID: " + usuarioRegistrador));
        demanda.setUsuarioRegistrador(usuarioCriador);

        Organizacao organizacao = organizacaoRepository.findById(idOrganizacao)
                .orElseThrow(() -> new OrganizacaoInvalidaException("Organização não encontrada com o ID: " + idOrganizacao));
        demanda.setOrganizacao(organizacao);

        return demandaRepository.save(demanda);
    }

    // ATUALIZA DEMANDA
    @Transactional
    public Demanda atualizarDemanda(Demanda demanda, UUID idDemanda) {
        Demanda demandaSaved = demandaRepository.findById(idDemanda)
                .orElseThrow(() -> new DemandaInvalidaException("Demanda não encontrada para atualização com o ID: " + idDemanda));

        // ATRIBUTOS QUE PODEM SER ALTERADOS
        demandaSaved.setNome(demanda.getNome());
        demandaSaved.setOrcamento(demanda.getOrcamento());
        demandaSaved.setDescricao(demanda.getDescricao());
        demandaSaved.setCriterio(demanda.getCriterio());
        demandaSaved.setDataPrazoFinal(demanda.getDataPrazoFinal());

        return demandaRepository.save(demandaSaved);
    }

    // ATUALIZA STATUS DA DEMANDA
    @Transactional
    public Demanda atualizarStatusDemanda(UUID idDemanda, StatusDemanda novoStatus) {
        Demanda demanda = demandaRepository.findById(idDemanda)
                .orElseThrow(() -> new DemandaInvalidaException("Demanda não encontrada com o ID: " + idDemanda));
        demanda.setStatus(novoStatus);

        return demandaRepository.save(demanda);
    }

    // APROVA DEMANDA
    @Transactional
    public Demanda aprovarDemandaDemandante(UUID idDemanda, Boolean decisao) {
        Demanda demanda = demandaRepository.findById(idDemanda)
                .orElseThrow(() -> new DemandaInvalidaException("Demanda não encontrada com o ID: " + idDemanda));

        if (!demanda.getStatus().equals(StatusDemanda.AGUARDANDO_APROVACAO)) {
            throw new IllegalStateException("A demanda não está no status 'AGUARDANDO_APROVACAO' para ser aprovada.");
        }

        if (decisao) {
            demanda.setStatus(StatusDemanda.AGUARDANDO_PROPOSTA);
        } else {
            demanda.setStatus(StatusDemanda.NAO_APROVADA);
        }
        demanda.setDataAprovado(LocalDate.now());

        return demandaRepository.save(demanda);
    }

    // REMOVE DEMANDA
    @Transactional
    public void removerDemanda(UUID idDemanda) {
        if (!demandaRepository.existsById(idDemanda)) {
            throw new DemandaInvalidaException("Demanda não encontrada para exclusão com o ID: " + idDemanda);
        }
        demandaRepository.deleteById(idDemanda);
    }

    // ENVIA DEMANDA PARA UM GRUPO DE PESQUISA ESPECÍFICO
    @Transactional
    public void enviarDemandaParaGrupo(UUID idDemanda, UUID idGrupoPesquisa) {
        Demanda demanda = demandaRepository.findById(idDemanda)
                .orElseThrow(() -> new DemandaInvalidaException("Demanda não encontrada com o ID: " + idDemanda));

        GrupoPesquisa grupoPesquisa = grupoPesquisaRepository.findById(idGrupoPesquisa)
                .orElseThrow(() -> new GrupoPesquisaInvalidoException("Grupo de pesquisa não encontrado com o ID: " + idGrupoPesquisa));

        if (grupoPesquisa.getDemandas() != null && grupoPesquisa.getDemandas().contains(demanda)) {
            throw new DemandaInvalidaException("Demanda já foi enviada para este grupo de pesquisa");
        }

        grupoPesquisa.adicionarDemanda(demanda);
        grupoPesquisaRepository.save(grupoPesquisa);
    }
}