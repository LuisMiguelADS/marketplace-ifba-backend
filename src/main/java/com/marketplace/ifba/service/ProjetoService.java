package com.marketplace.ifba.service;

import com.marketplace.ifba.dto.ProjetoRequest;
import com.marketplace.ifba.dto.ProjetoResponse;
import com.marketplace.ifba.exception.DadoConflitoException;
import com.marketplace.ifba.exception.DadoNaoEncontradoException;
import com.marketplace.ifba.mapper.ProjetoMapper;
import com.marketplace.ifba.model.*;
import com.marketplace.ifba.model.enums.StatusProjeto;
import com.marketplace.ifba.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final OrganizacaoRepository organizacaoRepository;
    private final InstituicaoRepository instituicaoRepository;
    private final DemandaRepository demandaRepository;
    private final OfertaSolucaoRepository ofertaSolucaoRepository;
    private final GrupoPesquisaRepository grupoPesquisaRepository;
    private final ChatRepository chatRepository;
    private final ProjetoMapper projetoMapper;

    public ProjetoService(ProjetoRepository projetoRepository, OrganizacaoRepository organizacaoRepository,
                          InstituicaoRepository instituicaoRepository, DemandaRepository demandaRepository,
                          OfertaSolucaoRepository ofertaSolucaoRepository, GrupoPesquisaRepository grupoPesquisaRepository,
                          ChatRepository chatRepository, ProjetoMapper projetoMapper) {
        this.projetoRepository = projetoRepository;
        this.organizacaoRepository = organizacaoRepository;
        this.instituicaoRepository = instituicaoRepository;
        this.demandaRepository = demandaRepository;
        this.ofertaSolucaoRepository = ofertaSolucaoRepository;
        this.grupoPesquisaRepository = grupoPesquisaRepository;
        this.chatRepository = chatRepository;
        this.projetoMapper = projetoMapper;
    }

    // ---------- LEITURA

    // BUSCA PROJETO PELO SEU ID
    @Transactional(readOnly = true)
    public ProjetoResponse buscarProjetoPorId(UUID idProjeto) {
        Projeto projeto = projetoRepository.findById(idProjeto)
                .orElseThrow(() -> new DadoNaoEncontradoException("Projeto não encontrado com o ID: " + idProjeto));
        return projetoMapper.toDTO(projeto);
    }

    // BUSCA O PROJETO PELO SEU NOME
    @Transactional(readOnly = true)
    public ProjetoResponse buscarProjetoPorNome(String nome) {
        Projeto projeto = projetoRepository.findByNome(nome)
                .orElseThrow(() -> new DadoNaoEncontradoException("Projeto não encontrado com o nome: " + nome));
        return projetoMapper.toDTO(projeto);
    }

    // BUSCA PROJETO PELA SUA DEMANDA
    @Transactional(readOnly = true)
    public ProjetoResponse buscarProjetoPorDemanda(UUID idDemanda) {
        Projeto projeto = projetoRepository.findAll().stream()
                .filter(proj -> proj.getDemanda().getIdDemanda().equals(idDemanda))
                .findFirst().orElseThrow(() -> new DadoNaoEncontradoException("Projeto não encontrado para a Demanda com o ID: " + idDemanda));
        return projetoMapper.toDTO(projeto);
    }

    // BUSCA PROJETO PELO GRUPO DE PESQUISA
    @Transactional(readOnly = true)
    public List<ProjetoResponse> buscarProjetoPorGrupoPesquisa(UUID idGrupoPesquisa) {
        return projetoRepository.findAll().stream()
                .filter(projeto -> projeto.getGrupoPesquisa().getIdGrupoPesquisa().equals(idGrupoPesquisa))
                .map(projetoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // BUSCA PROJETO PELA INSTITUIÇÃO
    @Transactional(readOnly = true)
    public List<ProjetoResponse> buscarProjetoPorInstituicao(UUID idInstituicao) {
        return projetoRepository.findAll().stream()
                .filter(projeto -> projeto.getInstituicao().getIdInstituicao().equals(idInstituicao))
                .map(projetoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // BUSCA PROJETO PELA ORGANIZAÇÃO
    @Transactional(readOnly = true)
    public List<ProjetoResponse> buscarProjetoPorOrganizacao(UUID idOrganizacao) {
        return projetoRepository.findAll().stream()
                .filter(projeto -> projeto.getOrganizacao().getIdOrganizacao().equals(idOrganizacao))
                .map(projetoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // LISTA TODOS OS PROJETOS DO SISTEMA
    @Transactional(readOnly = true)
    public List<ProjetoResponse> buscarTodosProjetos() {
        return projetoRepository.findAll().stream()
                .map(projetoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ---------- ESCRITA

    // REGISTRA PROJETO
    @Transactional
    public ProjetoResponse registrarProjeto(ProjetoRequest request) {
        if (projetoRepository.findAll().stream()
                .filter(projeto -> projeto.getDemanda().getIdDemanda().equals(request.idDemanda()))
                .findFirst()
                .isPresent()) {
            throw new DadoConflitoException("A Demanda com o ID: '" + request.idDemanda() + "' já está associada a outro projeto.");
        }

        Projeto projeto = projetoMapper.toEntity(request);
        projeto.setStatus(StatusProjeto.DESENVOLVENDO);

        Organizacao organizacao = organizacaoRepository.findById(request.idOrganizacao())
                .orElseThrow(() -> new DadoNaoEncontradoException("Organização não encontrada com o ID: " + request.idOrganizacao()));
        projeto.setOrganizacao(organizacao);

        Instituicao instituicao = instituicaoRepository.findById(request.idInstituicao())
                .orElseThrow(() -> new DadoNaoEncontradoException("Instituição não encontrada com o ID: " + request.idInstituicao()));
        projeto.setInstituicao(instituicao);

        Demanda demanda = demandaRepository.findById(request.idDemanda())
                .orElseThrow(() -> new DadoNaoEncontradoException("Demanda não encontrada com o ID: " + request.idDemanda()));
        projeto.setDemanda(demanda);

        if (request.idOfertaSolucao() != null) {
            OfertaSolucao ofertaSolucao = ofertaSolucaoRepository.findById(request.idOfertaSolucao())
                    .orElseThrow(() -> new DadoNaoEncontradoException("Oferta de Solução não encontrada com o ID: " + request.idOfertaSolucao()));
            if (projetoRepository.findAll().stream()
                    .filter(proj -> projeto.getSolucaoOferta().getIdSolucao().equals(ofertaSolucao.getIdSolucao()))
                    .findFirst()
                    .isPresent()) {
                throw new DadoConflitoException("A Oferta de Solução com o ID: '" + request.idOfertaSolucao() + "' já está associada a outro projeto.");
            }
            projeto.setSolucaoOferta(ofertaSolucao);
        }

        GrupoPesquisa grupoPesquisa = grupoPesquisaRepository.findById(request.idGrupoPesquisa())
                .orElseThrow(() -> new DadoNaoEncontradoException("Grupo de pesquisa não encontrado com o ID: " + request.idGrupoPesquisa()));
        projeto.setGrupoPesquisa(grupoPesquisa);

        Chat chat = new Chat();
        chat.setDataCriacao(LocalDateTime.now());

        List<ChatUsuario> chatUsuarios = new ArrayList<>();
        for (User userChat : grupoPesquisa.getUsuarios()) {
            ChatUsuario chatUsuario = new ChatUsuario();
            chatUsuario.setUsuario(userChat);
            chatUsuario.setChat(chat);
            chatUsuarios.add(chatUsuario);
        }
        chat.setChatUsuarios(chatUsuarios);

        projeto.setChat(chat);
        chat.setProjeto(projeto);

        return projetoMapper.toDTO(projetoRepository.save(projeto));
    }

    // ATUALIZA NOME DO PROJETO
    @Transactional
    public ProjetoResponse atualizarNomeProjeto(UUID idProjeto, String novoNome) {
        Projeto projeto = projetoRepository.findById(idProjeto)
                .orElseThrow(() -> new DadoNaoEncontradoException("Projeto não encontrado para atualização com o ID: " + idProjeto));

        if (!projeto.getNome().equals(novoNome)) {
            projetoRepository.findByNome(novoNome).ifPresent(p -> {
                if (!p.getIdProjeto().equals(idProjeto)) {
                    throw new DadoConflitoException("Já existe outro projeto com o nome: '" + novoNome + "'.");
                }
            });
        }

        projeto.setNome(novoNome);
        return projetoMapper.toDTO(projetoRepository.save(projeto));
    }

    // ATUALIZA STATUS DO PROJETO
    @Transactional
    public ProjetoResponse atualizarStatusProjeto(UUID idProjeto, StatusProjeto novoStatus) {
        Projeto projeto = projetoRepository.findById(idProjeto)
                .orElseThrow(() -> new DadoNaoEncontradoException("Projeto não encontrado com o ID: " + idProjeto));

        projeto.setStatus(novoStatus);
        return projetoMapper.toDTO(projetoRepository.save(projeto));
    }

    // REMOVE PROJETO PELO SEU ID
    @Transactional
    public void removerProjeto(UUID idProjeto) {
        if (!projetoRepository.existsById(idProjeto)) {
            throw new DadoNaoEncontradoException("Projeto não encontrado para exclusão com o ID: " + idProjeto);
        }
        projetoRepository.deleteById(idProjeto);
    }
}
