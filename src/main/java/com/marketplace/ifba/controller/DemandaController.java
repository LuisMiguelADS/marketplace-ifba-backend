package com.marketplace.ifba.controller;

import com.marketplace.ifba.dto.AprovarOuReprovarDemandaRequest;
import com.marketplace.ifba.dto.AtualizarStatusDemandaRequest;
import com.marketplace.ifba.dto.DemandaRequest;
import com.marketplace.ifba.dto.DemandaResponse;
import com.marketplace.ifba.dto.EnviarDemandaParaGrupoRequest;
import com.marketplace.ifba.mapper.DemandaMapper;
import com.marketplace.ifba.service.DemandaService;
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
@RequestMapping("/demandas")
@Tag(name = "Demanda", description = "Gerencia as operações relacionadas às demandas no marketplace.")
public class DemandaController {

    private final DemandaService demandaService;
    private final DemandaMapper demandaMapper;

    public DemandaController(DemandaService demandaService, DemandaMapper demandaMapper) {
        this.demandaService = demandaService;
        this.demandaMapper = demandaMapper;
    }

    @Operation(summary = "Retorna demanda a partir do ID", description = "Procura uma demanda salva com o ID informado")
    @GetMapping("/{idDemanda}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<DemandaResponse> buscarDemandaPorId(@PathVariable UUID idDemanda) {
        return ResponseEntity.ok(demandaMapper.toDTO(demandaService.buscarDemandaPorId(idDemanda)));
    }

    @Operation(summary = "Retorna demanda a partir do NOME", description = "Procura uma demanda salva com o NOME informado")
    @GetMapping("/nome/{nome}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<DemandaResponse> buscarDemandaPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(demandaMapper.toDTO(demandaService.buscarDemandaPorNome(nome)));
    }

    @Operation(summary = "Retorna demandas a partir da ORGANIZAÇÃO associada", description = "Procura uma demanda salva com a ORGANIZAÇÃO(que criou a demanda) associada a DEMANDA")
    @GetMapping("/organizacao/{idOrganizacao}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<List<DemandaResponse>> buscarDemandasPorOrganizacao(@PathVariable UUID idOrganizacao) {
        return ResponseEntity.ok(demandaService.buscarDemandasPorOrganizacao(idOrganizacao).stream().map(demandaMapper::toDTO).toList());
    }

    @Operation(summary = "Retorna demandas aprovadas a partir da ORGANIZAÇÃO associada", description = "Procura uma demanda salva com a ORGANIZAÇÃO(que criou a demanda) associada a DEMANDA")
    @GetMapping("/aprovadas")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<List<DemandaResponse>> buscarDemandasAprovadas() {
        return ResponseEntity.ok(demandaService.buscarDemandasAprovadas().stream().map(demandaMapper::toDTO).toList());
    }

    @Operation(summary = "Retorna todas as demandas", description = "Retorna todas as demandas cadastradas")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<List<DemandaResponse>> buscarTodasDemandas() {
        return ResponseEntity.ok(demandaService.buscarTodasDemandas().stream().map(demandaMapper::toDTO).toList());
    }

    @Operation(summary = "Registra demanda", description = "Realiza registro da demanda se passar das regras de negócio")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('EXTERNO')")
    public ResponseEntity<DemandaResponse> registrarDemanda(@RequestBody @Valid DemandaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(demandaMapper.toDTO(demandaService.registrarDemanda(demandaMapper.toEntity(request), request.idUsuarioRegistrador(), request.idOrganizacao())));
    }

    @Operation(summary = "Atualiza demanda", description = "Atualiza demanda se passar das regras de negócio")
    @PutMapping("/{idDemanda}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EXTERNO')")
    public ResponseEntity<DemandaResponse> atualizarDemanda(@RequestBody @Valid DemandaRequest request, @PathVariable UUID idDemanda) {
        return ResponseEntity.ok(demandaMapper.toDTO(demandaService.atualizarDemanda(demandaMapper.toEntity(request), idDemanda)));
    }

    @Operation(summary = "Atualiza status da demanda", description = "Atualiza o status da demanda se passar das regras de negócio")
    @PatchMapping("/{idDemanda}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DemandaResponse> atualizarStatusDemanda(@PathVariable UUID idDemanda, @RequestBody @Valid AtualizarStatusDemandaRequest request) {
        return ResponseEntity.ok(demandaMapper.toDTO(demandaService.atualizarStatusDemanda(idDemanda, request.novoStatus())));
    }

    @Operation(summary = "Aprova demanda", description = "Aprova a demanda")
    @PatchMapping("/aprovar-reprovar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DemandaResponse> aprovarDemandaDemandante(@RequestBody @Valid AprovarOuReprovarDemandaRequest request) {
        return ResponseEntity.ok(demandaMapper.toDTO(demandaService.aprovarDemandaDemandante(request.idDemanda(), request.decisao())));
    }

    @Operation(summary = "Remove demanda", description = "Remove o cadastro da demanda")
    @DeleteMapping("/{idDemanda}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EXTERNO')")
    public ResponseEntity<Void> removerDemanda(@PathVariable UUID idDemanda) {
        demandaService.removerDemanda(idDemanda);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Envia demanda para um grupo de pesquisa", description = "Envia uma demanda específica para um grupo de pesquisa")
    @PostMapping("/enviar-grupo")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EXTERNO')")
    public ResponseEntity<Void> enviarDemandaParaGrupo(@RequestBody @Valid EnviarDemandaParaGrupoRequest request) {
        demandaService.enviarDemandaParaGrupo(request.idDemanda(), request.idGrupoPesquisa());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Adiciona visualização à demanda", description = "Incrementa o contador de visualizações de uma demanda específica")
    @PostMapping("/visualizacao/{idDemanda}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<Void> adicionarVisualizacao(@PathVariable UUID idDemanda) {
        demandaService.incrementarVizualizacao(idDemanda);
        return ResponseEntity.ok().build();
    }
}