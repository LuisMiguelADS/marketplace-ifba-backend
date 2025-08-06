package com.marketplace.ifba.controller;

import com.marketplace.ifba.dto.AtualizarNomeProjetoRequest;
import com.marketplace.ifba.dto.AtualizarStatusProjetoRequest;
import com.marketplace.ifba.dto.ProjetoRequest;
import com.marketplace.ifba.dto.ProjetoResponse;
import com.marketplace.ifba.mapper.ProjetoMapper;
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
    private final ProjetoMapper projetoMapper;

    public ProjetoController(ProjetoService projetoService, ProjetoMapper projetoMapper) {
        this.projetoService = projetoService;
        this.projetoMapper = projetoMapper;
    }

    @Operation(summary = "Retorna projeto a partir do ID", description = "Procura um projeto com o ID informado")
    @GetMapping("/{idProjeto}")
    public ResponseEntity<ProjetoResponse> buscarProjetoPorId(@PathVariable UUID idProjeto) {
        return ResponseEntity.ok(projetoMapper.toDTO(projetoService.buscarProjetoPorId(idProjeto)));
    }

    @Operation(summary = "Retorna projeto a partir do NOME", description = "Procura um projeto salvo com o NOME informado")
    @GetMapping("/nome/{nome}")
    public ResponseEntity<ProjetoResponse> buscarProjetoPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(projetoMapper.toDTO(projetoService.buscarProjetoPorNome(nome)));
    }

    @Operation(summary = "Retorna projeto a partir da DEMANDA associada", description = "Procura um projeto salvo com a DEMANDA informada")
    @GetMapping("/demanda/{idDemanda}")
    public ResponseEntity<ProjetoResponse> buscarProjetoPorDemanda(@PathVariable UUID idDemanda) {
        return ResponseEntity.ok(projetoMapper.toDTO(projetoService.buscarProjetoPorDemanda(idDemanda)));
    }

    @Operation(summary = "Retorna projeto a partir da ORGANIZAÇÃO associada", description = "Procura um projeto salvo com a ORGANIZAÇÃO informada")
    @GetMapping("/organizacao/{idOrganizacao}")
    public ResponseEntity<List<ProjetoResponse>> buscarProjetoPorOrganizacao(@PathVariable UUID idOrganizacao) {
        return ResponseEntity.ok(projetoService.buscarProjetosPorOrganizacao(idOrganizacao).stream().map(projetoMapper::toDTO).toList());
    }

    @Operation(summary = "Retorna projeto a partir da INSTITUIÇÃO associada", description = "Procura um projeto salvo com a INSTITUIÇÃO informada")
    @GetMapping("/instituicao/{idInstituicao}")
    public ResponseEntity<List<ProjetoResponse>> buscarProjetoPorInstituicao(@PathVariable UUID idInstituicao) {
        return ResponseEntity.ok(projetoService.buscarProjetosPorInstituicao(idInstituicao).stream().map(projetoMapper::toDTO).toList());
    }

    @Operation(summary = "Retorna projeto a partir do GRUPO PESQUISA associado", description = "Procura um projeto salvo com o GRUPO PESQUISA informado")
    @GetMapping("/grupoPesquisa/{idGrupoPesquisa}")
    public ResponseEntity<List<ProjetoResponse>> buscarProjetoPorGrupoPesquisa(@PathVariable UUID idGrupoPesquisa) {
        return ResponseEntity.ok(projetoService.buscarProjetosPorGrupoPesquisa(idGrupoPesquisa).stream().map(projetoMapper::toDTO).toList());
    }

    @Operation(summary = "Retorna todos os projeto", description = "Retorna todos os projeto cadastrados")
    @GetMapping
    public ResponseEntity<List<ProjetoResponse>> buscarTodosProjetos() {
        return ResponseEntity.ok(projetoService.buscarTodosProjetos().stream().map(projetoMapper::toDTO).toList());
    }

    @Operation(summary = "Registra novo projeto", description = "Realiza registro do projeto se passar das regras de negócio")
    @PostMapping
    public ResponseEntity<ProjetoResponse> registrarProjeto(@RequestBody @Valid ProjetoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projetoMapper.toDTO(projetoService.registrarProjeto(projetoMapper.toEntity(request), request.idOrganizacao(), request.idInstituicao(), request.idDemanda(), request.idOfertaSolucao(), request.idGrupoPesquisa())));
    }

    @Operation(summary = "Atualiza nome do projeto", description = "Atualiza nome do projeto se passar das regras de negócio")
    @PatchMapping("/atualizar")
    public ResponseEntity<ProjetoResponse> atualizarNomeProjeto(@RequestBody @Valid AtualizarNomeProjetoRequest request) {
        return ResponseEntity.ok(projetoMapper.toDTO(projetoService.atualizarNomeProjeto(request.idProjeto(), request.novoNome())));
    }

    @Operation(summary = "Atualiza status do projeto", description = "Atualiza status do projeto se passar das regras de negócio")
    @PatchMapping("/novoStatus")
    public ResponseEntity<ProjetoResponse> atualizarStatusProjeto(@RequestBody @Valid AtualizarStatusProjetoRequest request) {
        return ResponseEntity.ok(projetoMapper.toDTO(projetoService.atualizarStatusProjeto(request.idProjeto(), request.novoStatus())));
    }

    @Operation(summary = "Remove projeto", description = "Remove o cadastro do projeto")
    @DeleteMapping("/{idProjeto}")
    public ResponseEntity<Void> removerProjeto(@PathVariable UUID idProjeto) {
        projetoService.removerProjeto(idProjeto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
