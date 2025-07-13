package com.marketplace.ifba.controller;

import com.marketplace.ifba.dto.TagRequest;
import com.marketplace.ifba.dto.TagResponse;
import com.marketplace.ifba.service.TagService;
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
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TagResponse> criarTag(@RequestBody @Valid TagRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tagService.salvarTag(request));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<TagResponse>> listarTodasTags() {
        List<TagResponse> tags = tagService.listarTags();

        return ResponseEntity.ok(tags);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<TagResponse> buscarTagPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(tagService.buscarTagPorID(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TagResponse> atualizarTag(@PathVariable UUID id, @RequestBody @Valid TagRequest request) {
        return ResponseEntity.ok(tagService.atualizarTag(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removerTag(@PathVariable UUID id) {
        tagService.removerTag(id);

        return ResponseEntity.noContent().build();
    }
}
