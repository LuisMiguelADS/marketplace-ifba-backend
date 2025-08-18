package com.marketplace.ifba.controller;

import com.marketplace.ifba.dto.AreaRequest;
import com.marketplace.ifba.dto.AreaResponse;
import com.marketplace.ifba.mapper.AreaMapper;
import com.marketplace.ifba.service.AreaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/area")
@Tag(name = "Tag", description = "Gerencia as operações relacionadas às tags no marketplace.")
public class AreaController {
    private final AreaService areaService;
    private final AreaMapper areaMapper;

    public AreaController(AreaService areaService, AreaMapper areaMapper) {
        this.areaService = areaService;
        this.areaMapper = areaMapper;
    }

    @Operation(summary = "Retorna tag a partir do ID", description = "Procura uma tag com o ID informado")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<AreaResponse> buscarTagPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(areaMapper.toDTO(areaService.buscarAreaPorID(id)));
    }

    @Operation(summary = "Retorna todas as tags", description = "Retorna todas as tags cadastradas")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<List<AreaResponse>> listarTodasTags() {
        return ResponseEntity.ok(areaService.buscarTodasAreas().stream().map(areaMapper::toDTO).toList());
    }

    @Operation(summary = "Registra tag", description = "Realiza registro da tag se passar das regras de negócio")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AreaResponse> criarTag(@RequestBody @Valid AreaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(areaMapper.toDTO(areaService.registrarArea(areaMapper.toEntity(request))));
    }

    @Operation(summary = "Atualiza tag", description = "Realiza registro da tag se passar das regras de negócio")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AreaResponse> atualizarTag(@PathVariable UUID id, @RequestBody @Valid AreaRequest request) {
        return ResponseEntity.ok(areaMapper.toDTO(areaService.atualizarTag(id, areaMapper.toEntity(request))));
    }

    @Operation(summary = "Remove tag", description = "Remove o cadastro da tag")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removerTag(@PathVariable UUID id) {
        areaService.removerTag(id);
        return ResponseEntity.noContent().build();
    }
}
