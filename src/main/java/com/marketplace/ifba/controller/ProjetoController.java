package com.marketplace.ifba.controller;

import com.marketplace.ifba.dto.AtualizarStatusProjetoRequest;
import com.marketplace.ifba.dto.ProjetoRequest;
import com.marketplace.ifba.dto.ProjetoResponse;
import com.marketplace.ifba.service.ProjetoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projetos")
public class ProjetoController {

    private final ProjetoService projetoService;

    public ProjetoController(ProjetoService projetoService) {
        this.projetoService = projetoService;
    }

    @GetMapping("/{idProjeto}")
    public ResponseEntity<ProjetoResponse> buscarProjetoPorId(@PathVariable UUID idProjeto) {
        return ResponseEntity.ok(projetoService.buscarProjetoPorId(idProjeto));
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<ProjetoResponse> buscarProjetoPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(projetoService.buscarProjetoPorNome(nome));
    }

    @GetMapping("/por-demanda/{idDemanda}")
    public ResponseEntity<ProjetoResponse> buscarProjetoPorDemanda(@PathVariable UUID idDemanda) {
        return ResponseEntity.ok(projetoService.buscarProjetoPorDemanda(idDemanda));
    }

    @GetMapping
    public ResponseEntity<List<ProjetoResponse>> buscarTodosProjetos() {
        return ResponseEntity.ok(projetoService.buscarTodosProjetos());
    }

    @PostMapping
    public ResponseEntity<ProjetoResponse> registrarProjeto(@RequestBody @Valid ProjetoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projetoService.registrarProjeto(request));
    }

    @PutMapping("/{idProjeto}/{novoNome}")
    public ResponseEntity<ProjetoResponse> atualizarNomeProjeto(@PathVariable String novoNome, @PathVariable UUID idProjeto) {
        return ResponseEntity.ok(projetoService.atualizarNomeProjeto(idProjeto, novoNome));
    }

    @PatchMapping("/{idProjeto}/status")
    public ResponseEntity<ProjetoResponse> atualizarStatusProjeto(@PathVariable UUID idProjeto, @RequestBody @Valid AtualizarStatusProjetoRequest request) {
        return ResponseEntity.ok(projetoService.atualizarStatusProjeto(idProjeto, request.novoStatus()));
    }

    @DeleteMapping("/{idProjeto}")
    public ResponseEntity<Void> removerProjeto(@PathVariable UUID idProjeto) {
        projetoService.removerProjeto(idProjeto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
