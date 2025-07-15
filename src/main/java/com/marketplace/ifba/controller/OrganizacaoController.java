package com.marketplace.ifba.controller;

import com.marketplace.ifba.dto.AprovarOrganizacaoRequest;
import com.marketplace.ifba.dto.OrganizacaoRequest;
import com.marketplace.ifba.dto.OrganizacaoResponse;
import com.marketplace.ifba.service.OrganizacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/organizacoes")
public class OrganizacaoController {

    private final OrganizacaoService organizacaoService;

    public OrganizacaoController(OrganizacaoService organizacaoService) {
        this.organizacaoService = organizacaoService;
    }

    @GetMapping("/{idOrganizacao}")
    public ResponseEntity<OrganizacaoResponse> buscarOrganizacaoPorId(@PathVariable UUID idOrganizacao) {
        return ResponseEntity.ok(organizacaoService.buscarOrganizacaoPorId(idOrganizacao));
    }

    @GetMapping("/name/{nome}")
    public ResponseEntity<OrganizacaoResponse> buscarOrganizacaoPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(organizacaoService.buscarOrganizacaoPorNome(nome));
    }

    @GetMapping
    public ResponseEntity<List<OrganizacaoResponse>> buscarTodasOrganizacoes() {
        return ResponseEntity.ok(organizacaoService.buscarTodasOrganizacoes());
    }

    @PostMapping("/{idUsuarioRegistrador}")
    public ResponseEntity<OrganizacaoResponse> registrarOrganizacao(@RequestBody @Valid OrganizacaoRequest request, @PathVariable UUID idUsuarioRegistrador) {
        return ResponseEntity.status(HttpStatus.CREATED).body(organizacaoService.registrarOrganizacao(request, idUsuarioRegistrador));
    }

    @PutMapping("/{idOrganizacao}")
    public ResponseEntity<OrganizacaoResponse> atualizarOrganizacao(@RequestBody @Valid OrganizacaoRequest request, @PathVariable UUID idOrganizacao) {
        return ResponseEntity.ok(organizacaoService.atualizarOrganizacao(idOrganizacao, request));
    }

    @PostMapping("/aprovar")
    public ResponseEntity<OrganizacaoResponse> aprovarOrganizacao(@RequestBody @Valid AprovarOrganizacaoRequest request) {
        return ResponseEntity.ok(organizacaoService.aprovarOrganizacao(request.idOrganizacao(), request.idAdmAprovador()));
    }

    @DeleteMapping("/{idOrganizacao}")
    public ResponseEntity<Void> deletarOrganizacao(@PathVariable UUID idOrganizacao) {
        organizacaoService.deletarOrganizacao(idOrganizacao);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
// eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJtYXJrZXRwbGFjZS1pZmJhIiwic3ViIjoiYWRtaW4udGVzdGVAZXhhbXBsZS5jb20iLCJleHAiOjE3NTI1Njc1NTh9.mpZzAW0pts77nWRP_hvqd4DkO-G67XWgWwa3jr0a4K8