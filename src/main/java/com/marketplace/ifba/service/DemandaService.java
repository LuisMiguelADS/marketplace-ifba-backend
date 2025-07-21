package com.marketplace.ifba.service;

import com.marketplace.ifba.dto.DemandaRequest;
import com.marketplace.ifba.dto.DemandaResponse;
import com.marketplace.ifba.exception.DadoConflitoException;
import com.marketplace.ifba.exception.DadoNaoEncontradoException;
import com.marketplace.ifba.mapper.DemandaMapper;
import com.marketplace.ifba.model.Demanda;
import com.marketplace.ifba.model.Organizacao;
import com.marketplace.ifba.model.User;
import com.marketplace.ifba.model.enums.StatusDemanda;
import com.marketplace.ifba.repository.DemandaRepository;
import com.marketplace.ifba.repository.OrganizacaoRepository;
import com.marketplace.ifba.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DemandaService {

    private final DemandaRepository demandaRepository;
    private final UserRepository userRepository;
    private final OrganizacaoRepository organizacaoRepository;
    private final DemandaMapper demandaMapper;

    public DemandaService(DemandaRepository demandaRepository, UserRepository userRepository,
                          OrganizacaoRepository organizacaoRepository, DemandaMapper demandaMapper) {
        this.demandaRepository = demandaRepository;
        this.userRepository = userRepository;
        this.organizacaoRepository = organizacaoRepository;
        this.demandaMapper = demandaMapper;
    }

    // ---------- LEITURA

    // BUSCA DEMANDA PELO SEU ID
    @Transactional(readOnly = true)
    public DemandaResponse buscarDemandaPorId(UUID idDemanda) {
        Demanda demanda = demandaRepository.findById(idDemanda)
                .orElseThrow(() -> new DadoNaoEncontradoException("Demanda não encontrada com o ID: " + idDemanda));
        return demandaMapper.toDTO(demanda);
    }

    // BUSCA DEMANDA POR NOME
    @Transactional(readOnly = true)
    public DemandaResponse buscarDemandaPorNome(String nome) {
        Demanda demanda = demandaRepository.findByNome(nome)
                .orElseThrow(() -> new DadoNaoEncontradoException("Demanda não encontrada com o nome: " + nome));
        return demandaMapper.toDTO(demanda);
    }

    // REALIZA A ADIÇÃO DE VISUALIZAÇÕES
    @Transactional
    public void incrementarVizualizacao(UUID idDemanda) {
        Demanda demanda = demandaRepository.findById(idDemanda)
                .orElseThrow(() -> new DadoNaoEncontradoException("Demanda não encontrada com o ID: " + idDemanda));
        demanda.setVizualizacoes(demanda.getVizualizacoes() + 1);
        demandaRepository.save(demanda);
    }

    // LISTA DEMANDAS PELA ORGANIZAÇÃO
    @Transactional(readOnly = true)
    public List<DemandaResponse> buscarDemandasPorOrganizacao(UUID idOrganizacao) {
        if (!organizacaoRepository.existsById(idOrganizacao)) {
            throw new DadoNaoEncontradoException("Organização não encontrada com o ID: " + idOrganizacao);
        }
        return demandaRepository.findAll().stream()
                .filter(demanda -> demanda.getOrganizacao().getIdOrganizacao().equals(idOrganizacao))
                .map(demandaMapper::toDTO)
                .collect(Collectors.toList());
    }

    // LISTA TODAS DEMANDAS
    @Transactional(readOnly = true)
    public List<DemandaResponse> buscarTodasDemandas() {
        return demandaRepository.findAll().stream()
                .map(demandaMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ---------- ESCRITA

    // REGITRA DEMANDA
    @Transactional
    public DemandaResponse registrarDemanda(DemandaRequest request) {
        if (demandaRepository.findByNome(request.nome()).isPresent()) {
            throw new DadoConflitoException("Já existe uma demanda com o nome: '" + request.nome() + "'.");
        }

        Demanda demanda = demandaMapper.toEntity(request);
        demanda.setStatus(StatusDemanda.AGUARDANDO_APROVACAO);
        demanda.setAprovacaoDemandante(false);
        demanda.setVizualizacoes(0);

        User usuarioCriador = userRepository.findById(request.idUsuarioCriador())
                .orElseThrow(() -> new DadoNaoEncontradoException("Usuário criador não encontrado com o ID: " + request.idUsuarioCriador()));
        demanda.setUsuarioCriador(usuarioCriador);

        Organizacao organizacao = organizacaoRepository.findById(request.idOrganizacao())
                .orElseThrow(() -> new DadoNaoEncontradoException("Organização não encontrada com o ID: " + request.idOrganizacao()));
        demanda.setOrganizacao(organizacao);

        return demandaMapper.toDTO(demandaRepository.save(demanda));
    }

    // ATUALIZA DEMANDA
    @Transactional
    public DemandaResponse atualizarDemanda(UUID idDemanda, DemandaRequest request) {
        Demanda demandaExistente = demandaRepository.findById(idDemanda)
                .orElseThrow(() -> new DadoNaoEncontradoException("Demanda não encontrada para atualização com o ID: " + idDemanda));

        if (!demandaExistente.getNome().equals(request.nome())) {
            demandaRepository.findByNome(request.nome()).ifPresent(d -> {
                if (!d.getIdDemanda().equals(idDemanda)) {
                    throw new DadoConflitoException("Já existe outra demanda com o nome: '" + request.nome() + "'.");
                }
            });
        }

        demandaMapper.updateEntityFromRequest(request, demandaExistente);

        return demandaMapper.toDTO(demandaRepository.save(demandaExistente));
    }

    // ATUALIZA STATUS DA DEMANDA
    @Transactional
    public DemandaResponse atualizarStatusDemanda(UUID idDemanda, StatusDemanda novoStatus) {
        Demanda demanda = demandaRepository.findById(idDemanda)
                .orElseThrow(() -> new DadoNaoEncontradoException("Demanda não encontrada com o ID: " + idDemanda));

        demanda.setStatus(novoStatus);
        return demandaMapper.toDTO(demandaRepository.save(demanda));
    }

    // APROVA DEMANDA
    @Transactional
    public DemandaResponse aprovarDemandaDemandante(UUID idDemanda, Boolean aprovado) {
        Demanda demanda = demandaRepository.findById(idDemanda)
                .orElseThrow(() -> new DadoNaoEncontradoException("Demanda não encontrada com o ID: " + idDemanda));

        if (aprovado && demanda.getAprovacaoDemandante()) {
            throw new IllegalStateException("Demanda já aprovada pelo demandante.");
        }
        if (!aprovado && !demanda.getAprovacaoDemandante()) {
            throw new IllegalStateException("Demanda já está como não aprovada pelo demandante.");
        }

        demanda.setAprovacaoDemandante(aprovado);
        if (aprovado) {
            demanda.setDataAprovado(LocalDateTime.now());
        } else {
            demanda.setDataAprovado(null);
        }

        return demandaMapper.toDTO(demandaRepository.save(demanda));
    }

    // REMOVE DEMANDA
    @Transactional
    public void removerDemanda(UUID idDemanda) {
        if (!demandaRepository.existsById(idDemanda)) {
            throw new DadoNaoEncontradoException("Demanda não encontrada para exclusão com o ID: " + idDemanda);
        }
        demandaRepository.deleteById(idDemanda);
    }
}