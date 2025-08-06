package com.marketplace.ifba.controller;

import com.marketplace.ifba.dto.AtualizarStatusGrupoPesquisaRequest;
import com.marketplace.ifba.dto.GrupoPesquisaRequest;
import com.marketplace.ifba.dto.GrupoPesquisaResponse;
import com.marketplace.ifba.mapper.GrupoPesquisaMapper;
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
    private final GrupoPesquisaMapper grupoPesquisaMapper;

    public GrupoPesquisaController(GrupoPesquisaService grupoPesquisaService, GrupoPesquisaMapper grupoPesquisaMapper) {
        this.grupoPesquisaService = grupoPesquisaService;
        this.grupoPesquisaMapper = grupoPesquisaMapper;
    }

    @Operation(summary = "Retorna grupo de pesquisa a partir do ID", description = "Procura um grupo de pesquisa com o ID informado")
    @GetMapping("/{idGrupoPesquisa}")
    public ResponseEntity<GrupoPesquisaResponse> buscarGrupoPesquisaPorId(@PathVariable UUID idGrupoPesquisa) {
        return ResponseEntity.ok(grupoPesquisaMapper.toDTO(grupoPesquisaService.buscarGrupoPesquisaPorId(idGrupoPesquisa)));
    }

    @Operation(summary = "Retorna grupo de pesquisa a partir do NOME", description = "Procura um grupo de pesquisa salva com o NOME informado")
    @GetMapping("/nome/{nome}")
    public ResponseEntity<GrupoPesquisaResponse> buscarGrupoPesquisaPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(grupoPesquisaMapper.toDTO(grupoPesquisaService.buscarGrupoPesquisaPorNome(nome)));
    }

    @Operation(summary = "Retorna grupo de pesquisa a partir da INSTITUIÇÃO associada", description = "Procura um demanda salva com a INSTITUIÇÃO associada ao grupo de pesquisa")
    @GetMapping("/instituicao/{idInstituicao}")
    public ResponseEntity<List<GrupoPesquisaResponse>> buscarGruposPorInstituicao(@PathVariable UUID idInstituicao) {
        return ResponseEntity.ok(grupoPesquisaService.buscarGruposPorInstituicao(idInstituicao).stream().map(grupoPesquisaMapper::toDTO).toList());
    }

    @Operation(summary = "Retorna todos os grupos de pesquisa", description = "Retorna todas os grupos de pesquisa cadastradas")
    @GetMapping
    public ResponseEntity<List<GrupoPesquisaResponse>> buscarTodosGruposPesquisa() {
        return ResponseEntity.ok(grupoPesquisaService.buscarTodosGruposPesquisa().stream().map(grupoPesquisaMapper::toDTO).toList());
    }

    @Operation(summary = "Registra grupo de pesquisa", description = "Realiza registro do grupo de pesquisa se passar das regras de negócio")
    @PostMapping
    public ResponseEntity<GrupoPesquisaResponse> registrarGrupoPesquisa(@RequestBody @Valid GrupoPesquisaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(grupoPesquisaMapper.toDTO(grupoPesquisaService.registrarGrupoPesquisa(grupoPesquisaMapper.toEntity(request), request.idInstituicao(), request.usuarioRegistrador(), request.idsAreas())));
    }

    @Operation(summary = "Atualiza grupo de pesquisa", description = "Atualiza grupo de pesquisa se passar das regras de negócio")
    @PutMapping()
    public ResponseEntity<GrupoPesquisaResponse> atualizarGrupoPesquisa(@RequestBody @Valid GrupoPesquisaRequest request) {
        return ResponseEntity.ok(grupoPesquisaMapper.toDTO(grupoPesquisaService.atualizarGrupoPesquisa(request.idGrupoPesquisa(), grupoPesquisaMapper.toEntity(request))));
    }

    @Operation(summary = "Atualiza status grupo de pesquisa", description = "Atualiza status do grupo de pesquisa se passar das regras de negócio")
    @PatchMapping("/status/{idGrupoPesquisa}")
    public ResponseEntity<GrupoPesquisaResponse> atualizarStatusGrupoPesquisa(@PathVariable UUID idGrupoPesquisa, @RequestBody @Valid AtualizarStatusGrupoPesquisaRequest request) {
        return ResponseEntity.ok(grupoPesquisaMapper.toDTO(grupoPesquisaService.atualizarStatusGrupoPesquisa(idGrupoPesquisa, request.novoStatus())));
    }

    @Operation(summary = "Remove grupo de pesquisa", description = "Remove o cadastro do grupo de pesquisa")
    @DeleteMapping("/{idGrupoPesquisa}")
    public ResponseEntity<Void> removerGrupoPesquisa(@PathVariable UUID idGrupoPesquisa) {
        grupoPesquisaService.removerGrupoPesquisa(idGrupoPesquisa);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
