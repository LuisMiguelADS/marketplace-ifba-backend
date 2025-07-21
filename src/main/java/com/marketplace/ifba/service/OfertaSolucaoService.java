package com.marketplace.ifba.service;

import com.marketplace.ifba.exception.DadoNaoEncontradoException;
import com.marketplace.ifba.mapper.OfertaSolucaoMapper;
import com.marketplace.ifba.model.OfertaSolucao;
import com.marketplace.ifba.dto.OfertaSolucaoRequest;
import com.marketplace.ifba.dto.OfertaSolucaoResponse;
import com.marketplace.ifba.model.enums.StatusOfertaSolucao;
import com.marketplace.ifba.repository.OfertaSolucaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OfertaSolucaoService {

    private final OfertaSolucaoRepository ofertaSolucaoRepository;
    private final OfertaSolucaoMapper ofertaSolucaoMapper;

    public OfertaSolucaoService(OfertaSolucaoRepository ofertaSolucaoRepository, OfertaSolucaoMapper ofertaSolucaoMapper) {
        this.ofertaSolucaoRepository = ofertaSolucaoRepository;
        this.ofertaSolucaoMapper = ofertaSolucaoMapper;
    }

    // ---------- LEITURA

    // BUSCA OFERTA SOLUÇÃO PELO SEU ID
    @Transactional(readOnly = true)
    public OfertaSolucaoResponse buscarOfertaSolucaoPorId(UUID idSolucao) {
        return ofertaSolucaoMapper.toDTO(ofertaSolucaoRepository.findById(idSolucao)
                .orElseThrow(() -> new DadoNaoEncontradoException("Não foi encontrada oferta de solução com o ID: " + idSolucao)));
    }


    // LISTA TODAS OFERTAS SOLUÇÃO
    @Transactional(readOnly = true)
    public List<OfertaSolucaoResponse> buscarTodasOfertasSolucao() {
        return ofertaSolucaoRepository.findAll().stream()
                .map(ofertaSolucaoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // BUSCA OFERTA SOLUÇÃO PELO SEU NOME
    @Transactional(readOnly = true)
    public List<OfertaSolucaoResponse> buscarOfertasSolucaoPorNome(String nome) {
        List<OfertaSolucao> solucoes = ofertaSolucaoRepository.findByNome(nome);
        if (solucoes.isEmpty()) {
            throw new DadoNaoEncontradoException("Nenhuma oferta de solução encontrada com o nome: " + nome);
        }
        return solucoes.stream()
                .map(ofertaSolucaoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // LISTA OFERTAS SOLUÇÃO PELO SEU STATUS
    @Transactional(readOnly = true)
    public List<OfertaSolucaoResponse> buscarOfertasSolucaoPorStatus(StatusOfertaSolucao status) {
        List<OfertaSolucao> solucoes = ofertaSolucaoRepository.findByStatus(status);
        if (solucoes.isEmpty()) {
            throw new DadoNaoEncontradoException("Nenhuma oferta de solução encontrada com o status: " + status);
        }
        return solucoes.stream()
                .map(ofertaSolucaoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // LISTA OFERTAS SOLUÇÃO QUE ESTÃO APROVADAS PELO ADM
    @Transactional(readOnly = true)
    public List<OfertaSolucaoResponse> buscarOfertasSolucaoAprovadas(Boolean aprovado) {
        List<OfertaSolucao> solucoes = ofertaSolucaoRepository.findByAprovado(aprovado);
        if (solucoes.isEmpty()) {
            String statusAprovacao = aprovado ? "aprovadas" : "não aprovadas";
            throw new DadoNaoEncontradoException("Nenhuma oferta de solução encontrada com o status de aprovação: " + statusAprovacao);
        }
        return solucoes.stream()
                .map(ofertaSolucaoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ---------- ESCRITA


    // REGISTRA OFERTA SOLUÇÃO
    @Transactional
    public OfertaSolucaoResponse registrarOfertaSolucao(OfertaSolucaoRequest request) {
        OfertaSolucao ofertaSolucao = ofertaSolucaoMapper.toEntity(request);
        ofertaSolucao.setDataRegistro(LocalDateTime.now());
        ofertaSolucao.setStatus(StatusOfertaSolucao.ENVIADA);
        ofertaSolucao.setAprovado(false);

        return ofertaSolucaoMapper.toDTO(ofertaSolucaoRepository.save(ofertaSolucao));
    }

    // ATUALIZA OFERTA SOLUÇÃO
    @Transactional
    public OfertaSolucaoResponse atualizarOfertaSolucao(UUID idSolucao, OfertaSolucaoRequest request) {
        OfertaSolucao ofertaSolucaoExistente = ofertaSolucaoRepository.findById(idSolucao)
                .orElseThrow(() -> new DadoNaoEncontradoException("Não foi encontrada oferta de solução com o ID: " + idSolucao));

        ofertaSolucaoMapper.updateEntityFromRequest(request, ofertaSolucaoExistente);

        return ofertaSolucaoMapper.toDTO(ofertaSolucaoRepository.save(ofertaSolucaoExistente));
    }

    // APROVA OFERTA SOLUÇÃO PELA ORGANIZAÇÃO
    @Transactional
    public OfertaSolucaoResponse aprovarOfertaSolucao(UUID idSolucao) {
        OfertaSolucao ofertaSolucao = ofertaSolucaoRepository.findById(idSolucao)
                .orElseThrow(() -> new DadoNaoEncontradoException("Oferta de solução não encontrada para aprovação com o ID: " + idSolucao));

        if (!"PENDENTE_APROVACAO".equals(ofertaSolucao.getStatus())) {
            throw new IllegalStateException("A oferta de solução não está no status 'PENDENTE_APROVACAO' para ser aprovada.");
        }

        ofertaSolucao.setStatus(StatusOfertaSolucao.APROVADA);
        ofertaSolucao.setAprovado(true);
        ofertaSolucao.setDataAprovacao(LocalDateTime.now());

        return ofertaSolucaoMapper.toDTO(ofertaSolucaoRepository.save(ofertaSolucao));
    }

    // REPROVA OFERTA SOLUÇÃO PELA ORGANIZAÇÃO
    @Transactional
    public OfertaSolucaoResponse reprovarOfertaSolucao(UUID idSolucao) {
        OfertaSolucao ofertaSolucao = ofertaSolucaoRepository.findById(idSolucao)
                .orElseThrow(() -> new DadoNaoEncontradoException("Oferta de solução não encontrada para reprovação com o ID: " + idSolucao));

        if (!"PENDENTE_APROVACAO".equals(ofertaSolucao.getStatus())) {
            throw new IllegalStateException("A oferta de solução não está no status 'PENDENTE_APROVACAO' para ser reprovada.");
        }

        ofertaSolucao.setStatus(StatusOfertaSolucao.NAO_APROVADA);
        ofertaSolucao.setAprovado(false);
        return ofertaSolucaoMapper.toDTO(ofertaSolucaoRepository.save(ofertaSolucao));
    }

    // REMOVER OFERTA SOLUÇÃO
    @Transactional
    public void removerOfertaSolucao(UUID idSolucao) {
        if (!ofertaSolucaoRepository.existsById(idSolucao)) {
            throw new DadoNaoEncontradoException("Oferta de solução não encontrada para exclusão com o ID: " + idSolucao);
        }
        ofertaSolucaoRepository.deleteById(idSolucao);
    }
}

