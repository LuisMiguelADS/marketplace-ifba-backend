package com.marketplace.ifba.service;

import com.marketplace.ifba.exception.DemandaInvalidaException;
import com.marketplace.ifba.exception.GrupoPesquisaInvalidoException;
import com.marketplace.ifba.exception.OfertaSolucaoInvalidaException;
import com.marketplace.ifba.model.Demanda;
import com.marketplace.ifba.model.GrupoPesquisa;
import com.marketplace.ifba.model.OfertaSolucao;
import com.marketplace.ifba.model.enums.StatusOfertaSolucao;
import com.marketplace.ifba.repository.DemandaRepository;
import com.marketplace.ifba.repository.GrupoPesquisaRepository;
import com.marketplace.ifba.repository.OfertaSolucaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OfertaSolucaoService {

    private final OfertaSolucaoRepository ofertaSolucaoRepository;
    private final DemandaRepository demandaRepository;
    private final GrupoPesquisaRepository grupoPesquisaRepository;

    public OfertaSolucaoService(OfertaSolucaoRepository ofertaSolucaoRepository, DemandaRepository demandaRepository, GrupoPesquisaRepository grupoPesquisaRepository) {
        this.ofertaSolucaoRepository = ofertaSolucaoRepository;
        this.demandaRepository = demandaRepository;
        this.grupoPesquisaRepository = grupoPesquisaRepository;
    }

    // ---------- LEITURA

    // BUSCA OFERTA SOLUÇÃO PELO SEU ID
    @Transactional(readOnly = true)
    public OfertaSolucao buscarOfertaSolucaoPorId(UUID idOfertaSolucao) {
        return ofertaSolucaoRepository.findById(idOfertaSolucao).orElseThrow(() -> new OfertaSolucaoInvalidaException("Oferta Solução não encontrada com o ID"));
    }

    // BUSCA OFERTA SOLUÇÃO PELO SEU NOME
    @Transactional(readOnly = true)
    public OfertaSolucao buscarOfertasSolucaoPorNome(String nome) {
        return ofertaSolucaoRepository.findAll().stream().filter(ofertaSolucao -> ofertaSolucao.getNome().equals(nome)).findFirst()
                .orElseThrow(() -> new OfertaSolucaoInvalidaException("Oferta Solução não encontrada com o NOME"));
    }

    // LISTA OFERTAS SOLUÇÃO PELO SEU STATUS
    @Transactional(readOnly = true)
    public List<OfertaSolucao> buscarOfertasSolucaoPorStatus(StatusOfertaSolucao status) {
        return ofertaSolucaoRepository.findAll().stream().filter(ofertaSolucao -> ofertaSolucao.getStatus().equals(status)).toList();
    }

    // LISTA OFERTAS SOLUÇÃO PELO SEU GRUPO PESQUISA
    @Transactional(readOnly = true)
    public List<OfertaSolucao> buscarOfertasSolucaoPorGrupoPesquisa(UUID idGrupoPesquisa) {
        if (!grupoPesquisaRepository.existsById(idGrupoPesquisa)) {
            throw new GrupoPesquisaInvalidoException("Grupo Pesquisa não encontrada com o ID");
        }
        return ofertaSolucaoRepository.findAll().stream().filter(ofertaSolucao -> ofertaSolucao.getGrupoPesquisa().getIdGrupoPesquisa().equals(idGrupoPesquisa)).toList();
    }

    // LISTA OFERTAS SOLUÇÃO PELA DEMANDA
    @Transactional(readOnly = true)
    public List<OfertaSolucao> buscarOfertasSolucaoPorDemanda(UUID idDemanda) {
        if (!demandaRepository.existsById(idDemanda)) {
            throw new DemandaInvalidaException("Demanda não encontrada com o ID");
        }
        return ofertaSolucaoRepository.findAll().stream().filter(ofertaSolucao -> ofertaSolucao.getDemanda().getIdDemanda().equals(idDemanda)).toList();
    }

    // LISTA TODAS OFERTAS SOLUÇÃO
    @Transactional(readOnly = true)
    public List<OfertaSolucao> buscarTodasOfertasSolucao() {
        return ofertaSolucaoRepository.findAll();
    }

    // ---------- ESCRITA

    // REGISTRA OFERTA SOLUÇÃO
    @Transactional
    public OfertaSolucao registrarOfertaSolucao(UUID idDemanda, UUID idGrupoPesquisa, OfertaSolucao ofertaSolucao) {

        Demanda demanda = demandaRepository.findById(idDemanda)
                .orElseThrow(() -> new DemandaInvalidaException("Demanda não encontrada com ID"));
        demanda.getOfertasSolucoes().add(ofertaSolucao);
        ofertaSolucao.setDemanda(demanda);

        GrupoPesquisa grupoPesquisa = grupoPesquisaRepository.findById(idGrupoPesquisa)
                        .orElseThrow(() -> new GrupoPesquisaInvalidoException("Grupo Pesquisa não encontrado com ID"));
        ofertaSolucao.setGrupoPesquisa(grupoPesquisa);

        ofertaSolucao.setDataRegistro(LocalDateTime.now());
        ofertaSolucao.setStatus(StatusOfertaSolucao.AGUARDANDO_APROVACAO);

        return ofertaSolucaoRepository.save(ofertaSolucao);
    }

    // ATUALIZA OFERTA SOLUÇÃO
    @Transactional
    public OfertaSolucao atualizarOfertaSolucao(UUID idOfertaSolucao, OfertaSolucao ofertaSolucao) {
        OfertaSolucao ofertaSolucaoSaved = ofertaSolucaoRepository.findById(idOfertaSolucao)
                .orElseThrow(() -> new OfertaSolucaoInvalidaException("Não foi encontrada oferta de solução com o ID"));

        // ATRIBUTOS QUE PODEM SER ALTERADOS
        ofertaSolucaoSaved.setNome(ofertaSolucao.getNome());
        ofertaSolucaoSaved.setDescricao(ofertaSolucao.getDescricao());
        ofertaSolucaoSaved.setPreco(ofertaSolucao.getPreco());
        ofertaSolucaoSaved.setPrazo(ofertaSolucao.getPrazo());
        ofertaSolucaoSaved.setResumo(ofertaSolucao.getResumo());
        ofertaSolucaoSaved.setRestricao(ofertaSolucao.getRestricao());
        ofertaSolucaoSaved.setRecursosNecessarios(ofertaSolucao.getRecursosNecessarios());

        return ofertaSolucaoRepository.save(ofertaSolucaoSaved);
    }

    // APROVA OFERTA SOLUÇÃO PELA ORGANIZAÇÃO
    @Transactional
    public OfertaSolucao aprovarOuReprovarOfertaSolucao(UUID idOfertaSolucao, Boolean decisao) {
        OfertaSolucao ofertaSolucaoSaved = ofertaSolucaoRepository.findById(idOfertaSolucao)
                .orElseThrow(() -> new OfertaSolucaoInvalidaException("Oferta de solução não encontrada para aprovação com o ID"));

        if (!StatusOfertaSolucao.AGUARDANDO_APROVACAO.equals(ofertaSolucaoSaved.getStatus())) {
            throw new IllegalStateException("O status não está em AGUARDANDO_APROVAÇÃO para aceitar está ação");
        }

        if (decisao) {
            ofertaSolucaoSaved.setStatus(StatusOfertaSolucao.APROVADA);
        } else {
            ofertaSolucaoSaved.setStatus(StatusOfertaSolucao.REPROVADA);
        }

        return ofertaSolucaoRepository.save(ofertaSolucaoSaved);
    }

    // REMOVER OFERTA SOLUÇÃO
    @Transactional
    public void removerOfertaSolucao(UUID idSolucao) {
        if (!ofertaSolucaoRepository.existsById(idSolucao)) {
            throw new OfertaSolucaoInvalidaException("Oferta de solução não encontrada para exclusão com o ID");
        }
        ofertaSolucaoRepository.deleteById(idSolucao);
    }
}

