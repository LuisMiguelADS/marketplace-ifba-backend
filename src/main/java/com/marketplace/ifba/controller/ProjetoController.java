package com.marketplace.ifba.controller;

import com.marketplace.ifba.dto.AtualizarStatusProjetoRequest;
import com.marketplace.ifba.dto.ProjetoRequest;
import com.marketplace.ifba.dto.ProjetoResponse;
import com.marketplace.ifba.service.ProjetoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projetos")
@Tag(name = "Projeto", description = "Gerencia as operações relacionadas aos projetos no marketplace.")
public class ProjetoController {

    private final ProjetoService projetoService;

    public ProjetoController(ProjetoService projetoService) {
        this.projetoService = projetoService;
    }

    @Operation(summary = "Retorna projeto a partir do ID", description = "Procura um projeto com o ID informado")
    @GetMapping("/{idProjeto}")
    public ResponseEntity<ProjetoResponse> buscarProjetoPorId(@PathVariable UUID idProjeto) {
        return ResponseEntity.ok(projetoService.buscarProjetoPorId(idProjeto));
    }

    @Operation(summary = "Retorna projeto a partir do NOME", description = "Procura um projeto salva com o NOME informado")
    @GetMapping("/nome/{nome}")
    public ResponseEntity<ProjetoResponse> buscarProjetoPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(projetoService.buscarProjetoPorNome(nome));
    }

    @Operation(summary = "Retorna projeto a partir da DEMANDA associada", description = "Procura um demanda salva com a DEMANDA associada ao projeto")
    @GetMapping("/demanda/{idDemanda}")
    public ResponseEntity<ProjetoResponse> buscarProjetoPorDemanda(@PathVariable UUID idDemanda) {
        return ResponseEntity.ok(projetoService.buscarProjetoPorDemanda(idDemanda));
    }

    @Operation(summary = "Retorna todos os projeto", description = "Retorna todos os projeto cadastrados")
    @GetMapping
    public ResponseEntity<List<ProjetoResponse>> buscarTodosProjetos() {
        return ResponseEntity.ok(projetoService.buscarTodosProjetos());
    }

    @Operation(summary = "Registra projeto", description = "Realiza registro do projeto se passar das regras de negócio")
    @PostMapping
    public ResponseEntity<ProjetoResponse> registrarProjeto(@RequestBody @Valid ProjetoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projetoService.registrarProjeto(request));
    }

    @Operation(summary = "Atualiza projeto de pesquisa", description = "Atualiza status do projeto se passar das regras de negócio")
    @PatchMapping("/{idProjeto}/status")
    public ResponseEntity<ProjetoResponse> atualizarStatusProjeto(@PathVariable UUID idProjeto, @RequestBody @Valid AtualizarStatusProjetoRequest request) {
        return ResponseEntity.ok(projetoService.atualizarStatusProjeto(idProjeto, request.novoStatus()));
    }

    @Operation(summary = "Remove projeto", description = "Remove o cadastro do projeto")
    @DeleteMapping("/{idProjeto}")
    public ResponseEntity<Void> removerProjeto(@PathVariable UUID idProjeto) {
        projetoService.removerProjeto(idProjeto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
