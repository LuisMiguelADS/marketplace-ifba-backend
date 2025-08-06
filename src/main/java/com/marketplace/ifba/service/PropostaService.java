package com.marketplace.ifba.service;

import com.marketplace.ifba.dto.PropostaRequest;
import com.marketplace.ifba.dto.PropostaResponse;
import com.marketplace.ifba.exception.*;
import com.marketplace.ifba.mapper.PropostaMapper;
import com.marketplace.ifba.model.GrupoPesquisa;
import com.marketplace.ifba.model.Instituicao;
import com.marketplace.ifba.model.Proposta;
import com.marketplace.ifba.model.enums.StatusOrganizacao;
import com.marketplace.ifba.model.enums.StatusProposta;
import com.marketplace.ifba.repository.GrupoPesquisaRepository;
import com.marketplace.ifba.repository.InstituicaoRepository;
import com.marketplace.ifba.repository.PropostaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PropostaService {

    private final PropostaRepository propostaRepository;
    private final GrupoPesquisaRepository grupoPesquisaRepository;
    private final InstituicaoRepository instituicaoRepository;
    private final PropostaMapper propostaMapper;

    public PropostaService(PropostaRepository propostaRepository,
                           GrupoPesquisaRepository grupoPesquisaRepository,
                           InstituicaoRepository instituicaoRepository,
                           PropostaMapper propostaMapper) {
        this.propostaRepository = propostaRepository;
        this.grupoPesquisaRepository = grupoPesquisaRepository;
        this.instituicaoRepository = instituicaoRepository;
        this.propostaMapper = propostaMapper;
    }

    // ---------- LEITURA

    // BUSCA PROPOSTA PELO SEU ID
    @Transactional(readOnly = true)
    public Proposta buscarPropostaPorId(UUID idProposta) {
        return propostaRepository.findById(idProposta)
                .orElseThrow(() -> new PropostaInvalidaException("Proposta não encontrada com o ID: " + idProposta));
    }

    // BUSCA PROPOSTA PELO SEU NOME
    @Transactional(readOnly = true)
    public Proposta buscarPropostaPorNome(String nome) {
        Proposta proposta = propostaRepository.findByNome(nome)
                .orElseThrow(() -> new PropostaInvalidaException("Proposta não encontrada com o nome: " + nome));
        return proposta;
    }

    // BUSCA PROPOSTA PELO ID DE UM GRUPO DE PESQUISA
    @Transactional(readOnly = true)
    public List<Proposta> buscarPropostasPorGrupoPesquisa(UUID idGrupoPesquisa) {
        if (!grupoPesquisaRepository.existsById(idGrupoPesquisa)) {
            throw new GrupoPesquisaInvalidoException("Grupo de pesquisa não encontrado com o ID: " + idGrupoPesquisa);
        }
        return propostaRepository.findAll().stream().filter(proposta -> proposta.getGrupoPesquisa().getIdGrupoPesquisa().equals(idGrupoPesquisa)).toList();
    }

    // BUSCA PROPOSTA PELO ID DE UMA INSTITUIÇÃO
    @Transactional(readOnly = true)
    public List<Proposta> buscarPropostasPorInstituicao(UUID idInstituicao) {
        if (!instituicaoRepository.existsById(idInstituicao)) {
            throw new InstituicaoInvalidaException("Instituição não encontrada com o ID: " + idInstituicao);
        }
        return propostaRepository.findAll().stream().filter(demanda -> demanda.getInstituicao().getIdInstituicao().equals(idInstituicao)).toList();
    }

    // LISTA TODAS AS PROPOSTAS DO SISTEMA
    @Transactional(readOnly = true)
    public List<Proposta> buscarTodasPropostas() {
        return propostaRepository.findAll();
    }

    // ---------- ESCRITA

    // REGISTRO PROPOSTA
    @Transactional
    public Proposta registrarProposta(Proposta proposta, UUID idInstituicao, UUID idGrupoPesquisa) {
        proposta.setDataRegistro(LocalDateTime.now());
        proposta.setStatus(StatusProposta.AGUARDANDO_APROVACAO);

        GrupoPesquisa grupoPesquisa = grupoPesquisaRepository.findById(idGrupoPesquisa)
                .orElseThrow(() -> new GrupoPesquisaInvalidoException("Grupo de pesquisa não encontrado!"));
        proposta.setGrupoPesquisa(grupoPesquisa);

        Instituicao instituicao = instituicaoRepository.findById(idInstituicao)
                .orElseThrow(() -> new InstituicaoInvalidaException("Instituição não encontrada!"));
        proposta.setInstituicao(instituicao);

        return propostaRepository.save(proposta);
    }

    // ATUALIZA PROPOSTA
    @Transactional
    public Proposta atualizarProposta(UUID idProposta, Proposta proposta) {
        Proposta propostaSaved = propostaRepository.findById(idProposta)
                .orElseThrow(() -> new PropostaInvalidaException("Proposta não encontrada para atualização!"));

        if (!propostaSaved.getNome().equals(proposta.getNome())) {
            throw new PropostaInvalidaException("Já existe outra proposta com esse nome!");
        }

        // ATRIBUTOS QUE PODEM SER ALTERADOS
        propostaSaved.setNome(proposta.getNome());
        propostaSaved.setDescricao(proposta.getDescricao());
        propostaSaved.setResumo(proposta.getResumo());
        propostaSaved.setOrcamento(proposta.getOrcamento());
        propostaSaved.setSolucao(proposta.getSolucao());
        propostaSaved.setRestricoes(proposta.getRestricoes());
        propostaSaved.setRecursosNecessarios(proposta.getRecursosNecessarios());

        return propostaRepository.save(propostaSaved);
    }

    // ATUALIZA O STATUS DA PROPOSTA
    @Transactional
    public Proposta aprovarOuReprovarProposta(UUID idProposta, Boolean decisao) {
        Proposta propostaSaved = propostaRepository.findById(idProposta)
                .orElseThrow(() -> new PropostaInvalidaException("Proposta não encontrada!"));

        if (!StatusProposta.AGUARDANDO_APROVACAO.equals(propostaSaved.getStatus())) {
            throw new PropostaInvalidaException("A proposta não está no status 'AGUARDANDO_APROVACAO' para ser aprovada.");
        }

        if (decisao) {
            propostaSaved.setStatus(StatusProposta.APROVADA);
        } else {
            propostaSaved.setStatus(StatusProposta.NAO_APROVADA);
        }

        return propostaRepository.save(propostaSaved);
    }

    // REMOVE PROPOSTA PELO SEU ID
    @Transactional
    public void removerProposta(UUID idProposta) {
        if (!propostaRepository.existsById(idProposta)) {
            throw new PropostaInvalidaException("Proposta não encontrada para exclusão com o ID: " + idProposta);
        }
        propostaRepository.deleteById(idProposta);
    }
}
