package com.marketplace.ifba.service;

import com.marketplace.ifba.exception.DadoConflitoException;
import com.marketplace.ifba.exception.DadoNaoEncontradoException;
import com.marketplace.ifba.model.Area;
import com.marketplace.ifba.repository.AreaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AreaService {

    private final AreaRepository areaRepository;

    public AreaService(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    // ---------- LEITURA

    // BUSCA TAG PELO SEU ID
    @Transactional(readOnly = true)
    public Area buscarTagPorID(UUID id) {
        return areaRepository.findById(id).orElseThrow(() -> new DadoNaoEncontradoException("Tag não encontrada com esse id!"));
    }

    // LISTA TODAS AS TAGS DO SISTEMA
    @Transactional(readOnly = true)
    public List<Area> buscarTodasTags() {
        return areaRepository.findAll();
    }

    // ---------- ESCRITA

    // REGISTRO DA TAG
    @Transactional()
    public Area registrarTag(Area area) {
        if (areaRepository.findAll().stream().anyMatch(tagSave -> tagSave.getNomeTag().equals(area.getNomeTag()))) {
            throw new DadoConflitoException("Já existe uma tag com o nome: '" + area.getNomeTag());
        }

        return areaRepository.save(area);
    }

    // ATUALIZA TAG
    @Transactional()
    public Area atualizarTag(UUID id, Area area) {
        Area areaSaved = areaRepository.findById(id).orElseThrow(() -> new DadoNaoEncontradoException("Tag não encontrada!"));

        // ATRIBUTOS QUE PODEM SER ALTERADOS
        areaSaved.setNomeTag(area.getNomeTag());

        return areaRepository.save(areaSaved);
    }

    // REMOVE TAG PELO SEU ID
    @Transactional()
    public void removerTag(UUID id) {
        if (!areaRepository.existsById(id)) {
            throw new DadoNaoEncontradoException("Tag não encontrada!");
        }
        areaRepository.deleteById(id);
    }
}