package com.marketplace.ifba.controller;

import com.marketplace.ifba.dto.TagRequest;
import com.marketplace.ifba.dto.TagResponse;
import com.marketplace.ifba.service.TagService;
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
@RequestMapping("/tag")
@Tag(name = "Tag", description = "Gerencia as operações relacionadas às tags no marketplace.")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @Operation(summary = "Retorna tag a partir do ID", description = "Procura uma tag com o ID informado")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<TagResponse> buscarTagPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(tagService.buscarTagPorID(id));
    }

    @Operation(summary = "Retorna todas as tags", description = "Retorna todas as tags cadastradas")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<TagResponse>> listarTodasTags() {
        List<TagResponse> tags = tagService.buscarTodasTags();

        return ResponseEntity.ok(tags);
    }

    @Operation(summary = "Registra tag", description = "Realiza registro da tag se passar das regras de negócio")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TagResponse> criarTag(@RequestBody @Valid TagRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tagService.registrarTag(request));
    }

    @Operation(summary = "Atualiza tag", description = "Realiza registro da tag se passar das regras de negócio")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TagResponse> atualizarTag(@PathVariable UUID id, @RequestBody @Valid TagRequest request) {
        return ResponseEntity.ok(tagService.atualizarTag(id, request));
    }

    @Operation(summary = "Remove tag", description = "Remove o cadastro da tag")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removerTag(@PathVariable UUID id) {
        tagService.removerTag(id);

        return ResponseEntity.noContent().build();
    }
}
