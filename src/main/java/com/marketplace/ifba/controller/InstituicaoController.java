package com.marketplace.ifba.controller;

import com.marketplace.ifba.dto.AprovarOuReprovarInstituicaoRequest;
import com.marketplace.ifba.dto.InstituicaoRequest;
import com.marketplace.ifba.dto.InstituicaoResponse;
import com.marketplace.ifba.mapper.InstituicaoMapper;
import com.marketplace.ifba.service.InstituicaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/instituicoes")
@Tag(name = "Instituição", description = "Gerencia as operações relacionadas às instituições no marketplace.")
public class InstituicaoController {

    private final InstituicaoService instituicaoService;
    private final InstituicaoMapper instituicaoMapper;

    public InstituicaoController(InstituicaoService instituicaoService, InstituicaoMapper instituicaoMapper) {
        this.instituicaoService = instituicaoService;
        this.instituicaoMapper = instituicaoMapper;
    }

    @Operation(summary = "Retorna instituição a partir do ID", description = "Procura uma instituição com o ID informado")
    @GetMapping("/{idInstituicao}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<InstituicaoResponse> buscarInstituicaoPorId(@PathVariable UUID idInstituicao) {
        return ResponseEntity.ok(instituicaoMapper.toDTO(instituicaoService.buscarInstituicaoPorId(idInstituicao)));
    }

    @Operation(summary = "Retorna instituição a partir do NOME", description = "Procura uma instituição salva com o NOME informado")
    @GetMapping("/name/{nome}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<InstituicaoResponse> buscarInstituicaoPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(instituicaoMapper.toDTO(instituicaoService.buscarInstituicaoPorNome(nome)));
    }

    @Operation(summary = "Retorna todas as instituições", description = "Retorna todas as instituições cadastradas")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<List<InstituicaoResponse>> buscarInstituicoes() {
        return ResponseEntity.ok(instituicaoService.buscarTodasInstituicoes().stream().map(instituicaoMapper::toDTO).toList());
    }

    @Operation(summary = "Registra instituição", description = "Realiza registro da instituição se passar das regras de negócio")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<InstituicaoResponse> registrarInstituicao(@RequestBody @Valid InstituicaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(instituicaoMapper.toDTO(instituicaoService.registrarInstituicao(instituicaoMapper.toEntity(request), request.idUsuarioRegistrador())));
    }

    @Operation(summary = "Atualiza instituição", description = "Realiza registro da instituição se passar das regras de negócio")
    @PutMapping("/{idInstituicao}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<InstituicaoResponse> atualizarInstituicao(@RequestBody @Valid InstituicaoRequest request, @PathVariable UUID idInstituicao) {
        return ResponseEntity.ok(instituicaoMapper.toDTO(instituicaoService.atualizarInstituicao(instituicaoMapper.toEntity(request), idInstituicao)));
    }

    @Operation(summary = "Aprova instituição", description = "Realiza registro da instituição se passar das regras de negócio")
    @PostMapping("/aprovar-reprovar")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<InstituicaoResponse> aprovaOuReprovaInstituicao(@RequestBody @Valid AprovarOuReprovarInstituicaoRequest request) {
        return ResponseEntity.ok(instituicaoMapper.toDTO(instituicaoService.aprovarOuReprovaInstituicao(request.idInstituicao(), request.idAdm(), request.decisao())));
    }

    @Operation(summary = "Remove instituição", description = "Remove o cadastro da instituição")
    @DeleteMapping("/{idInstituicao}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<Void> removerInstituicao(@PathVariable UUID idInstituicao) {
        instituicaoService.removerInstituicao(idInstituicao);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
