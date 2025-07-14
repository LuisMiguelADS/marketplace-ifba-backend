package com.marketplace.ifba.service;

import com.marketplace.ifba.dto.TagRequest;
import com.marketplace.ifba.dto.TagResponse;
import com.marketplace.ifba.mapper.TagMapper;
import com.marketplace.ifba.model.Tag;
import com.marketplace.ifba.repository.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TagService {

    @Autowired
    private final TagRepository tagRepository;

    @Autowired
    private final TagMapper tagMapper;

    public TagService(TagRepository tagRepository, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

    public TagResponse salvarTag(TagRequest request) {
        Tag tag = tagMapper.toEntity(request);
        tagRepository.save(tag);

        return tagMapper.toDTO(tag);
    }

    public List<TagResponse> listarTags() {
        return tagRepository.findAll().stream().map(tagMapper::toDTO).toList();
    }

    public TagResponse buscarTagPorID(UUID id) {
        return tagMapper.toDTO(tagRepository.findById(id).orElseThrow());
    }

    public void removerTag(UUID id) {
        if (!tagRepository.existsById(id)) {
            throw new EntityNotFoundException("Tag não encontrada! Id: " + id);
        }
        tagRepository.deleteById(id);
    }

    public TagResponse atualizarTag(UUID id, TagRequest request) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tag não encontrada!"));
        tagMapper.updateEntityFromRequest(request, tag);
        Tag tagAtualizada = tagRepository.save(tag);

        return tagMapper.toDTO(tagAtualizada);
    }
}