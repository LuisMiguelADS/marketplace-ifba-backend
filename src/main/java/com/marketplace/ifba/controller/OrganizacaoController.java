package com.marketplace.ifba.controller;

import com.marketplace.ifba.dto.AprovarOrganizacaoRequest;
import com.marketplace.ifba.dto.OrganizacaoRequest;
import com.marketplace.ifba.dto.OrganizacaoResponse;
import com.marketplace.ifba.service.OrganizacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/organizacoes")
@Tag(name = "Organização", description = "Gerencia as operações relacionadas às organizações no marketplace.")
public class OrganizacaoController {

    private final OrganizacaoService organizacaoService;

    public OrganizacaoController(OrganizacaoService organizacaoService) {
        this.organizacaoService = organizacaoService;
    }

    @Operation(summary = "Retorna organização a partir do ID", description = "Procura uma organização com o ID informado")
    @GetMapping("/{idOrganizacao}")
    public ResponseEntity<OrganizacaoResponse> buscarOrganizacaoPorId(@PathVariable UUID idOrganizacao) {
        return ResponseEntity.ok(organizacaoService.buscarOrganizacaoPorId(idOrganizacao));
    }

    @Operation(summary = "Retorna organização a partir do NOME", description = "Procura uma organização salva com o NOME informado")
    @GetMapping("/name/{nome}")
    public ResponseEntity<OrganizacaoResponse> buscarOrganizacaoPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(organizacaoService.buscarOrganizacaoPorNome(nome));
    }

    @Operation(summary = "Retorna todas as organizações", description = "Retorna todas as organizações cadastradas")
    @GetMapping
    public ResponseEntity<List<OrganizacaoResponse>> buscarTodasOrganizacoes() {
        return ResponseEntity.ok(organizacaoService.buscarTodasOrganizacoes());
    }

    @Operation(summary = "Registra organização", description = "Realiza registro da organização se passar das regras de negócio")
    @PostMapping("/{idUsuarioRegistrador}")
    public ResponseEntity<OrganizacaoResponse> registrarOrganizacao(@RequestBody @Valid OrganizacaoRequest request, @PathVariable UUID idUsuarioRegistrador) {
        return ResponseEntity.status(HttpStatus.CREATED).body(organizacaoService.registrarOrganizacao(request, idUsuarioRegistrador));
    }

    @Operation(summary = "Atualiza organização", description = "Realiza registro da organização se passar das regras de negócio")
    @PutMapping("/{idOrganizacao}")
    public ResponseEntity<OrganizacaoResponse> atualizarOrganizacao(@RequestBody @Valid OrganizacaoRequest request, @PathVariable UUID idOrganizacao) {
        return ResponseEntity.ok(organizacaoService.atualizarOrganizacao(idOrganizacao, request));
    }

    @Operation(summary = "Aprova organização", description = "Realiza registro da organização se passar das regras de negócio")
    @PostMapping("/aprovar")
    public ResponseEntity<OrganizacaoResponse> aprovarOrganizacao(@RequestBody @Valid AprovarOrganizacaoRequest request) {
        return ResponseEntity.ok(organizacaoService.aprovarOrganizacao(request.idOrganizacao(), request.idAdmAprovador()));
    }

    @Operation(summary = "Remove organização", description = "Remove o cadastro da organização")
    @DeleteMapping("/{idOrganizacao}")
    public ResponseEntity<Void> removerOrganizacao(@PathVariable UUID idOrganizacao) {
        organizacaoService.removerOrganizacao(idOrganizacao);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}