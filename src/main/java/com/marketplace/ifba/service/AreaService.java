package com.marketplace.ifba.service;

import com.marketplace.ifba.exception.AreaInvalidaException;
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

    // BUSCA AREA PELO SEU ID
    @Transactional(readOnly = true)
    public Area buscarAreaPorID(UUID id) {
        return areaRepository.findById(id).orElseThrow(() -> new AreaInvalidaException("Area não encontrada com esse id!"));
    }

    // LISTA TODAS AS AREAS DO SISTEMA
    @Transactional(readOnly = true)
    public List<Area> buscarTodasAreas() {
        return areaRepository.findAll();
    }

    // ---------- ESCRITA

    // REGISTRO DA AREA
    @Transactional()
    public Area registrarArea(Area area) {
        if (areaRepository.findAll().stream().anyMatch(areaSave -> areaSave.getNomeArea().equals(area.getNomeArea()))) {
            throw new AreaInvalidaException("Já existe uma area com o nome: '" + area.getNomeArea());
        }

        return areaRepository.save(area);
    }

    // ATUALIZA AREA
    @Transactional()
    public Area atualizarArea(UUID id, Area area) {
        Area areaSaved = areaRepository.findById(id).orElseThrow(() -> new AreaInvalidaException("Area não encontrada!"));

        // ATRIBUTOS QUE PODEM SER ALTERADOS
        areaSaved.setNomeArea(area.getNomeArea());

        return areaRepository.save(areaSaved);
    }

    // REMOVE AREA PELO SEU ID
    @Transactional()
    public void removerArea(UUID id) {
        if (!areaRepository.existsById(id)) {
            throw new AreaInvalidaException("Area não encontrada!");
        }
        areaRepository.deleteById(id);
    }
}