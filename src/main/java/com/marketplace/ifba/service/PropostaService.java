package com.marketplace.ifba.service;

import com.marketplace.ifba.dto.PropostaRequest;
import com.marketplace.ifba.dto.PropostaResponse;
import com.marketplace.ifba.exception.DadoConflitoException;
import com.marketplace.ifba.exception.DadoNaoEncontradoException;
import com.marketplace.ifba.mapper.PropostaMapper;
import com.marketplace.ifba.model.Instituicao;
import com.marketplace.ifba.model.Proposta;
import com.marketplace.ifba.model.enums.StatusProposta;
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
    //private final GrupoPesquisaRepository grupoPesquisaRepository;
    private final InstituicaoRepository instituicaoRepository;
    private final PropostaMapper propostaMapper;

    public PropostaService(PropostaRepository propostaRepository,
                           // GrupoPesquisaRepository grupoPesquisaRepository,
                           InstituicaoRepository instituicaoRepository,
                           PropostaMapper propostaMapper) {
        this.propostaRepository = propostaRepository;
        //this.grupoPesquisaRepository = grupoPesquisaRepository;
        this.instituicaoRepository = instituicaoRepository;
        this.propostaMapper = propostaMapper;
    }

    // Leitura

    @Transactional(readOnly = true)
    public PropostaResponse buscarPropostaPorId(UUID idProposta) {
        Proposta proposta = propostaRepository.findById(idProposta)
                .orElseThrow(() -> new DadoNaoEncontradoException("Proposta não encontrada com o ID: " + idProposta));
        return propostaMapper.toDTO(proposta);
    }

    @Transactional(readOnly = true)
    public List<PropostaResponse> buscarTodasPropostas() {
        return propostaRepository.findAll().stream()
                .map(propostaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PropostaResponse buscarPropostaPorNome(String nome) {
        Proposta proposta = propostaRepository.findByNome(nome)
                .orElseThrow(() -> new DadoNaoEncontradoException("Proposta não encontrada com o nome: " + nome));
        return propostaMapper.toDTO(proposta);
    }

    @Transactional(readOnly = true)
    public List<PropostaResponse> buscarPropostasPorGrupoPesquisa(UUID idGrupoPesquisa) {
        // Validação se o Grupo de Pesquisa existe
        // if (!grupoPesquisaRepository.existsById(idGrupoPesquisa)) {
        //     throw new DadoNaoEncontradoException("Grupo de pesquisa não encontrado com o ID: " + idGrupoPesquisa);
        // }
        return propostaRepository.findByGrupoPesquisaIdGrupo(idGrupoPesquisa).stream()
                .map(propostaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PropostaResponse> buscarPropostasPorInstituicao(UUID idInstituicao) {
        if (!instituicaoRepository.existsById(idInstituicao)) {
            throw new DadoNaoEncontradoException("Instituição não encontrada com o ID: " + idInstituicao);
        }
        return propostaRepository.findByInstituicaoIdInstituicao(idInstituicao).stream()
                .map(propostaMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Escrita

    @Transactional
    public PropostaResponse registrarProposta(PropostaRequest request) {
        if (propostaRepository.findByNome(request.nome()).isPresent()) {
            throw new DadoConflitoException("Já existe uma proposta com o nome: '" + request.nome() + "'.");
        }

        Proposta proposta = propostaMapper.toEntity(request);
        proposta.setDataRegistro(LocalDateTime.now());
        proposta.setStatus(StatusProposta.PENDENTE_APROVACAO);

        // GrupoPesquisa grupoPesquisa = grupoPesquisaRepository.findById(request.idGrupoPesquisa())
        //         .orElseThrow(() -> new DadoNaoEncontradoException("Grupo de pesquisa não encontrado com o ID: " + request.idGrupoPesquisa()));
        // proposta.setGrupoPesquisa(grupoPesquisa);

        Instituicao instituicao = instituicaoRepository.findById(request.idInstituicao())
                .orElseThrow(() -> new DadoNaoEncontradoException("Instituição não encontrada com o ID: " + request.idInstituicao()));

        proposta.setInstituicao(instituicao);

        return propostaMapper.toDTO(propostaRepository.save(proposta));
    }

    @Transactional
    public PropostaResponse atualizarProposta(UUID idProposta, PropostaRequest request) {
        Proposta propostaExistente = propostaRepository.findById(idProposta)
                .orElseThrow(() -> new DadoNaoEncontradoException("Proposta não encontrada para atualização com o ID: " + idProposta));

        if (!propostaExistente.getNome().equals(request.nome())) {
            propostaRepository.findByNome(request.nome()).ifPresent(p -> {
                if (!p.getIdProposta().equals(idProposta)) {
                    throw new DadoConflitoException("Já existe outra proposta com o nome: '" + request.nome() + "'.");
                }
            });
        }
        propostaMapper.updateEntityFromRequest(request, propostaExistente);

        return propostaMapper.toDTO(propostaRepository.save(propostaExistente));
    }

    @Transactional
    public PropostaResponse atualizarStatusProposta(UUID idProposta, StatusProposta novoStatus) {
        Proposta proposta = propostaRepository.findById(idProposta)
                .orElseThrow(() -> new DadoNaoEncontradoException("Proposta não encontrada com o ID: " + idProposta));

        proposta.setStatus(novoStatus);
        return propostaMapper.toDTO(propostaRepository.save(proposta));
    }

    @Transactional
    public void deletarProposta(UUID idProposta) {
        if (!propostaRepository.existsById(idProposta)) {
            throw new DadoNaoEncontradoException("Proposta não encontrada para exclusão com o ID: " + idProposta);
        }
        propostaRepository.deleteById(idProposta);
    }
}
