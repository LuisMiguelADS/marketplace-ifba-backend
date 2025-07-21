package com.marketplace.ifba.service;

import com.marketplace.ifba.dto.TagRequest;
import com.marketplace.ifba.dto.TagResponse;
import com.marketplace.ifba.mapper.TagMapper;
import com.marketplace.ifba.model.Tag;
import com.marketplace.ifba.repository.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public TagService(TagRepository tagRepository, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

    // ---------- LEITURA

    // BUSCA TAG PELO SEU ID
    @Transactional(readOnly = true)
    public TagResponse buscarTagPorID(UUID id) {
        return tagMapper.toDTO(tagRepository.findById(id).orElseThrow());
    }

    // LISTA TODAS AS TAGS DO SISTEMA
    @Transactional(readOnly = true)
    public List<TagResponse> buscarTodasTags() {
        return tagRepository.findAll().stream().map(tagMapper::toDTO).toList();
    }

    // ---------- ESCRITA

    // REGISTRO DA TAG
    @Transactional()
    public TagResponse registrarTag(TagRequest request) {
        Tag tag = tagMapper.toEntity(request);
        tagRepository.save(tag);

        return tagMapper.toDTO(tag);
    }

    // ATUALIZA TAG
    @Transactional()
    public TagResponse atualizarTag(UUID id, TagRequest request) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tag não encontrada!"));
        tagMapper.updateEntityFromRequest(request, tag);
        Tag tagAtualizada = tagRepository.save(tag);

        return tagMapper.toDTO(tagAtualizada);
    }

    // REMOVE TAG PELO SEU ID
    @Transactional()
    public void removerTag(UUID id) {
        if (!tagRepository.existsById(id)) {
            throw new EntityNotFoundException("Tag não encontrada! Id: " + id);
        }
        tagRepository.deleteById(id);
    }
}