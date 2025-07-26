package com.marketplace.ifba.controller;

import com.marketplace.ifba.dto.AtualizarStatusGrupoPesquisaRequest;
import com.marketplace.ifba.dto.GrupoPesquisaRequest;
import com.marketplace.ifba.dto.GrupoPesquisaResponse;
import com.marketplace.ifba.service.GrupoPesquisaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/grupos-pesquisa")
@Tag(name = "Grupo de Pesquisa", description = "Gerencia as operações relacionadas aos grupos de pesquisa no marketplace.")
public class GrupoPesquisaController {

    private final GrupoPesquisaService grupoPesquisaService;

    public GrupoPesquisaController(GrupoPesquisaService grupoPesquisaService) {
        this.grupoPesquisaService = grupoPesquisaService;
    }

    @Operation(summary = "Retorna grupo de pesquisa a partir do ID", description = "Procura um grupo de pesquisa com o ID informado")
    @GetMapping("/{idGrupoPesquisa}")
    public ResponseEntity<GrupoPesquisaResponse> buscarGrupoPesquisaPorId(@PathVariable UUID idGrupoPesquisa) {
        return ResponseEntity.ok(grupoPesquisaService.buscarGrupoPesquisaPorId(idGrupoPesquisa));
    }

    @Operation(summary = "Retorna grupo de pesquisa a partir do NOME", description = "Procura um grupo de pesquisa salva com o NOME informado")
    @GetMapping("/nome/{nome}")
    public ResponseEntity<GrupoPesquisaResponse> buscarGrupoPesquisaPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(grupoPesquisaService.buscarGrupoPesquisaPorNome(nome));
    }

    @Operation(summary = "Retorna grupo de pesquisa a partir da INSTITUIÇÃO associada", description = "Procura um demanda salva com a INSTITUIÇÃO associada ao grupo de pesquisa")
    @GetMapping("/instituicao/{idInstituicao}")
    public ResponseEntity<List<GrupoPesquisaResponse>> buscarGruposPorInstituicao(@PathVariable UUID idInstituicao) {
        return ResponseEntity.ok(grupoPesquisaService.buscarGruposPorInstituicao(idInstituicao));
    }

    @Operation(summary = "Retorna todos os grupos de pesquisa", description = "Retorna todas os grupos de pesquisa cadastradas")
    @GetMapping
    public ResponseEntity<List<GrupoPesquisaResponse>> buscarTodosGruposPesquisa() {
        return ResponseEntity.ok(grupoPesquisaService.buscarTodosGruposPesquisa());
    }

    @Operation(summary = "Registra grupo de pesquisa", description = "Realiza registro do grupo de pesquisa se passar das regras de negócio")
    @PostMapping
    public ResponseEntity<GrupoPesquisaResponse> registrarGrupoPesquisa(@RequestBody @Valid GrupoPesquisaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(grupoPesquisaService.registrarGrupoPesquisa(request));
    }

    @Operation(summary = "Atualiza grupo de pesquisa", description = "Atualiza grupo de pesquisa se passar das regras de negócio")
    @PutMapping("/{idGrupoPesquisa}")
    public ResponseEntity<GrupoPesquisaResponse> atualizarGrupoPesquisa(
            @RequestBody @Valid GrupoPesquisaRequest request,
            @PathVariable UUID idGrupoPesquisa) {
        return ResponseEntity.ok(grupoPesquisaService.atualizarGrupoPesquisa(idGrupoPesquisa, request));
    }

    @Operation(summary = "Atualiza status grupo de pesquisa", description = "Atualiza status do grupo de pesquisa se passar das regras de negócio")
    @PatchMapping("/{idGrupoPesquisa}/status")
    public ResponseEntity<GrupoPesquisaResponse> atualizarStatusGrupoPesquisa(
            @PathVariable UUID idGrupoPesquisa,
            @RequestBody @Valid AtualizarStatusGrupoPesquisaRequest request) {
        return ResponseEntity.ok(grupoPesquisaService.atualizarStatusGrupoPesquisa(idGrupoPesquisa, request.novoStatus()));
    }

    @Operation(summary = "Remove grupo de pesquisa", description = "Remove o cadastro do grupo de pesquisa")
    @DeleteMapping("/{idGrupoPesquisa}")
    public ResponseEntity<Void> removerGrupoPesquisa(@PathVariable UUID idGrupoPesquisa) {
        grupoPesquisaService.removerGrupoPesquisa(idGrupoPesquisa);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
