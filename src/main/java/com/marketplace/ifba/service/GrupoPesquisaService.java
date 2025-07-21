package com.marketplace.ifba.service;

import com.marketplace.ifba.dto.GrupoPesquisaRequest;
import com.marketplace.ifba.dto.GrupoPesquisaResponse;
import com.marketplace.ifba.exception.DadoConflitoException;
import com.marketplace.ifba.exception.DadoNaoEncontradoException;
import com.marketplace.ifba.mapper.GrupoPesquisaMapper;
import com.marketplace.ifba.model.GrupoPesquisa;
import com.marketplace.ifba.model.Instituicao;
import com.marketplace.ifba.model.Tag;
import com.marketplace.ifba.model.User;
import com.marketplace.ifba.model.enums.StatusGrupoPesquisa;
import com.marketplace.ifba.repository.GrupoPesquisaRepository;
import com.marketplace.ifba.repository.InstituicaoRepository;
import com.marketplace.ifba.repository.TagRepository;
import com.marketplace.ifba.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GrupoPesquisaService {

    private final GrupoPesquisaRepository grupoPesquisaRepository;
    private final InstituicaoRepository instituicaoRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final GrupoPesquisaMapper grupoPesquisaMapper;

    public GrupoPesquisaService(GrupoPesquisaRepository grupoPesquisaRepository,
                                InstituicaoRepository instituicaoRepository,
                                UserRepository userRepository,
                                TagRepository tagRepository,
                                GrupoPesquisaMapper grupoPesquisaMapper) {
        this.grupoPesquisaRepository = grupoPesquisaRepository;
        this.instituicaoRepository = instituicaoRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.grupoPesquisaMapper = grupoPesquisaMapper;
    }

    // Leitura

    @Transactional(readOnly = true)
    public GrupoPesquisaResponse buscarGrupoPesquisaPorId(UUID idGrupoPesquisa) {
        GrupoPesquisa grupo = grupoPesquisaRepository.findById(idGrupoPesquisa)
                .orElseThrow(() -> new DadoNaoEncontradoException("Grupo de pesquisa não encontrado com o ID: " + idGrupoPesquisa));
        return grupoPesquisaMapper.toDTO(grupo);
    }

    @Transactional(readOnly = true)
    public List<GrupoPesquisaResponse> buscarTodosGruposPesquisa() {
        return grupoPesquisaRepository.findAll().stream()
                .map(grupoPesquisaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GrupoPesquisaResponse buscarGrupoPesquisaPorNome(String nome) {
        GrupoPesquisa grupo = grupoPesquisaRepository.findByNome(nome)
                .orElseThrow(() -> new DadoNaoEncontradoException("Grupo de pesquisa não encontrado com o nome: " + nome));
        return grupoPesquisaMapper.toDTO(grupo);
    }

    @Transactional(readOnly = true)
    public List<GrupoPesquisaResponse> buscarGruposPorInstituicao(UUID idInstituicao) {
        if (!instituicaoRepository.existsById(idInstituicao)) {
            throw new DadoNaoEncontradoException("Instituição não encontrada com o ID: " + idInstituicao);
        }
        return grupoPesquisaRepository.findByInstituicaoIdInstituicao(idInstituicao).stream()
                .map(grupoPesquisaMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Escrita

    @Transactional
    public GrupoPesquisaResponse registrarGrupoPesquisa(GrupoPesquisaRequest request) {
        if (grupoPesquisaRepository.findByNome(request.nome()).isPresent()) {
            throw new DadoConflitoException("Já existe um grupo de pesquisa com o nome: '" + request.nome() + "'.");
        }

        GrupoPesquisa grupoPesquisa = grupoPesquisaMapper.toEntity(request);
        grupoPesquisa.setDataRegistro(LocalDateTime.now());
        grupoPesquisa.setStatus(StatusGrupoPesquisa.ATIVO);

        Instituicao instituicao = instituicaoRepository.findById(request.idInstituicao())
                .orElseThrow(() -> new DadoNaoEncontradoException("Instituição não encontrada com o ID: " + request.idInstituicao()));
        grupoPesquisa.setInstituicao(instituicao);

        if (request.idsUsuarios() != null && !request.idsUsuarios().isEmpty()) {
            List<User> usuarios = userRepository.findAllById(request.idsUsuarios());
            if (usuarios.size() != request.idsUsuarios().size()) {
                throw new DadoNaoEncontradoException("Um ou mais IDs de usuários fornecidos não foram encontrados.");
            }
            grupoPesquisa.setUsuarios(new ArrayList<>(usuarios));
        } else {
            grupoPesquisa.setUsuarios(new ArrayList<>());
        }

        if (request.idsTags() != null && !request.idsTags().isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(request.idsTags());
            if (tags.size() != request.idsTags().size()) {
                throw new DadoNaoEncontradoException("Um ou mais IDs de tags fornecidos não foram encontrados.");
            }
            grupoPesquisa.setTags(new ArrayList<>(tags));
        } else {
            grupoPesquisa.setTags(new ArrayList<>());
        }

        return grupoPesquisaMapper.toDTO(grupoPesquisaRepository.save(grupoPesquisa));
    }

    @Transactional
    public GrupoPesquisaResponse atualizarGrupoPesquisa(UUID idGrupoPesquisa, GrupoPesquisaRequest request) {
        GrupoPesquisa grupoExistente = grupoPesquisaRepository.findById(idGrupoPesquisa)
                .orElseThrow(() -> new DadoNaoEncontradoException("Grupo de pesquisa não encontrado para atualização com o ID: " + idGrupoPesquisa));

        if (!grupoExistente.getNome().equals(request.nome())) {
            grupoPesquisaRepository.findByNome(request.nome()).ifPresent(g -> {
                if (!g.getIdGrupoPesquisa().equals(idGrupoPesquisa)) {
                    throw new DadoConflitoException("Já existe outro grupo de pesquisa com o nome: '" + request.nome());
                }
            });
        }

        grupoPesquisaMapper.updateEntityFromRequest(request, grupoExistente);

        if (!grupoExistente.getInstituicao().getIdInstituicao().equals(request.idInstituicao())) {
            Instituicao novaInstituicao = instituicaoRepository.findById(request.idInstituicao())
                    .orElseThrow(() -> new DadoNaoEncontradoException("Nova instituição não encontrada com o ID: " + request.idInstituicao()));
            grupoExistente.setInstituicao(novaInstituicao);
        }

        if (request.idsUsuarios() != null) {
            List<User> novosUsuarios = userRepository.findAllById(request.idsUsuarios());
            if (novosUsuarios.size() != request.idsUsuarios().size()) {
                throw new DadoNaoEncontradoException("Um ou mais IDs de usuários fornecidos para atualização não foram encontrados.");
            }
            grupoExistente.setUsuarios(new ArrayList<>(novosUsuarios));
        }

        if (request.idsTags() != null) {
            List<Tag> novasTags = tagRepository.findAllById(request.idsTags());
            if (novasTags.size() != request.idsTags().size()) {
                throw new DadoNaoEncontradoException("Um ou mais IDs de tags fornecidos para atualização não foram encontrados.");
            }
            grupoExistente.setTags(new ArrayList<>(novasTags));
        }

        return grupoPesquisaMapper.toDTO(grupoPesquisaRepository.save(grupoExistente));
    }

    @Transactional
    public GrupoPesquisaResponse atualizarStatusGrupoPesquisa(UUID idGrupoPesquisa, StatusGrupoPesquisa novoStatus) {
        GrupoPesquisa grupo = grupoPesquisaRepository.findById(idGrupoPesquisa)
                .orElseThrow(() -> new DadoNaoEncontradoException("Grupo de pesquisa não encontrado com o ID: " + idGrupoPesquisa));
        grupo.setStatus(novoStatus);
        return grupoPesquisaMapper.toDTO(grupoPesquisaRepository.save(grupo));
    }

    @Transactional
    public void deletarGrupoPesquisa(UUID idGrupoPesquisa) {
        if (!grupoPesquisaRepository.existsById(idGrupoPesquisa)) {
            throw new DadoNaoEncontradoException("Grupo de pesquisa não encontrado para exclusão com o ID: " + idGrupoPesquisa);
        }
        grupoPesquisaRepository.deleteById(idGrupoPesquisa);
    }
}
