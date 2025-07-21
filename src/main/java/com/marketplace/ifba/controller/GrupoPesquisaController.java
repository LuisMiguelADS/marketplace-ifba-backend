package com.marketplace.ifba.controller;

import com.marketplace.ifba.dto.AtualizarStatusGrupoPesquisaRequest;
import com.marketplace.ifba.dto.GrupoPesquisaRequest;
import com.marketplace.ifba.dto.GrupoPesquisaResponse;
import com.marketplace.ifba.service.GrupoPesquisaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/grupos-pesquisa")
public class GrupoPesquisaController {

    private final GrupoPesquisaService grupoPesquisaService;

    public GrupoPesquisaController(GrupoPesquisaService grupoPesquisaService) {
        this.grupoPesquisaService = grupoPesquisaService;
    }

    @GetMapping("/{idGrupoPesquisa}")
    public ResponseEntity<GrupoPesquisaResponse> buscarGrupoPesquisaPorId(@PathVariable UUID idGrupoPesquisa) {
        return ResponseEntity.ok(grupoPesquisaService.buscarGrupoPesquisaPorId(idGrupoPesquisa));
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<GrupoPesquisaResponse> buscarGrupoPesquisaPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(grupoPesquisaService.buscarGrupoPesquisaPorNome(nome));
    }

    @GetMapping("/por-instituicao/{idInstituicao}")
    public ResponseEntity<List<GrupoPesquisaResponse>> buscarGruposPorInstituicao(@PathVariable UUID idInstituicao) {
        return ResponseEntity.ok(grupoPesquisaService.buscarGruposPorInstituicao(idInstituicao));
    }

    @GetMapping
    public ResponseEntity<List<GrupoPesquisaResponse>> buscarTodosGruposPesquisa() {
        return ResponseEntity.ok(grupoPesquisaService.buscarTodosGruposPesquisa());
    }

    @PostMapping
    public ResponseEntity<GrupoPesquisaResponse> registrarGrupoPesquisa(@RequestBody @Valid GrupoPesquisaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(grupoPesquisaService.registrarGrupoPesquisa(request));
    }

    @PutMapping("/{idGrupoPesquisa}")
    public ResponseEntity<GrupoPesquisaResponse> atualizarGrupoPesquisa(
            @RequestBody @Valid GrupoPesquisaRequest request,
            @PathVariable UUID idGrupoPesquisa) {
        return ResponseEntity.ok(grupoPesquisaService.atualizarGrupoPesquisa(idGrupoPesquisa, request));
    }

    @PatchMapping("/{idGrupoPesquisa}/status")
    public ResponseEntity<GrupoPesquisaResponse> atualizarStatusGrupoPesquisa(
            @PathVariable UUID idGrupoPesquisa,
            @RequestBody @Valid AtualizarStatusGrupoPesquisaRequest request) {
        return ResponseEntity.ok(grupoPesquisaService.atualizarStatusGrupoPesquisa(idGrupoPesquisa, request.novoStatus()));
    }


    @DeleteMapping("/{idGrupoPesquisa}")
    public ResponseEntity<Void> deletarGrupoPesquisa(@PathVariable UUID idGrupoPesquisa) {
        grupoPesquisaService.deletarGrupoPesquisa(idGrupoPesquisa);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
