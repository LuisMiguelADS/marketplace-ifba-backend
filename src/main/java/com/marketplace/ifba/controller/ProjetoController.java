package com.marketplace.ifba.controller;

import com.marketplace.ifba.dto.*;
import com.marketplace.ifba.mapper.EntregaMapper;
import com.marketplace.ifba.mapper.ProjetoMapper;
import com.marketplace.ifba.service.ProjetoService;
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
@RequestMapping("/projetos")
@Tag(name = "Projeto", description = "Gerencia as operações relacionadas aos projetos no marketplace.")
public class ProjetoController {

    private final ProjetoService projetoService;
    private final ProjetoMapper projetoMapper;
    private final EntregaMapper entregaMapper;

    public ProjetoController(ProjetoService projetoService, ProjetoMapper projetoMapper, EntregaMapper entregaMapper) {
        this.projetoService = projetoService;
        this.projetoMapper = projetoMapper;
        this.entregaMapper = entregaMapper;
    }

    @Operation(summary = "Retorna projeto a partir do ID", description = "Procura um projeto com o ID informado")
    @GetMapping("/{idProjeto}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<ProjetoResponse> buscarProjetoPorId(@PathVariable UUID idProjeto) {
        return ResponseEntity.ok(projetoMapper.toDTO(projetoService.buscarProjetoPorId(idProjeto)));
    }

    @Operation(summary = "Retorna projeto a partir do NOME", description = "Procura um projeto salvo com o NOME informado")
    @GetMapping("/nome/{nome}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<ProjetoResponse> buscarProjetoPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(projetoMapper.toDTO(projetoService.buscarProjetoPorNome(nome)));
    }

    @Operation(summary = "Retorna projeto a partir da DEMANDA associada", description = "Procura um projeto salvo com a DEMANDA informada")
    @GetMapping("/demanda/{idDemanda}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<ProjetoResponse> buscarProjetoPorDemanda(@PathVariable UUID idDemanda) {
        return ResponseEntity.ok(projetoMapper.toDTO(projetoService.buscarProjetoPorDemanda(idDemanda)));
    }

    @Operation(summary = "Retorna projeto a partir da ORGANIZAÇÃO associada", description = "Procura um projeto salvo com a ORGANIZAÇÃO informada")
    @GetMapping("/organizacao/{idOrganizacao}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<List<ProjetoResponse>> buscarProjetoPorOrganizacao(@PathVariable UUID idOrganizacao) {
        return ResponseEntity.ok(projetoService.buscarProjetosPorOrganizacao(idOrganizacao).stream().map(projetoMapper::toDTO).toList());
    }

    @Operation(summary = "Retorna projeto a partir da INSTITUIÇÃO associada", description = "Procura um projeto salvo com a INSTITUIÇÃO informada")
    @GetMapping("/instituicao/{idInstituicao}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<List<ProjetoResponse>> buscarProjetoPorInstituicao(@PathVariable UUID idInstituicao) {
        return ResponseEntity.ok(projetoService.buscarProjetosPorInstituicao(idInstituicao).stream().map(projetoMapper::toDTO).toList());
    }

    @Operation(summary = "Retorna projeto a partir do GRUPO PESQUISA associado", description = "Procura um projeto salvo com o GRUPO PESQUISA informado")
    @GetMapping("/grupoPesquisa/{idGrupoPesquisa}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<List<ProjetoResponse>> buscarProjetoPorGrupoPesquisa(@PathVariable UUID idGrupoPesquisa) {
        return ResponseEntity.ok(projetoService.buscarProjetosPorGrupoPesquisa(idGrupoPesquisa).stream().map(projetoMapper::toDTO).toList());
    }

    @Operation(summary = "Retorna todos os projeto", description = "Retorna todos os projeto cadastrados")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<List<ProjetoResponse>> buscarTodosProjetos() {
        return ResponseEntity.ok(projetoService.buscarTodosProjetos().stream().map(projetoMapper::toDTO).toList());
    }

    @Operation(summary = "Registra novo projeto", description = "Realiza registro do projeto se passar das regras de negócio")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjetoResponse> registrarProjeto(@RequestBody @Valid ProjetoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projetoMapper.toDTO(projetoService.registrarProjeto(projetoMapper.toEntity(request), request.idOrganizacao(), request.idInstituicao(), request.idDemanda(), request.idOfertaSolucao(), request.idGrupoPesquisa())));
    }

    @Operation(summary = "Atualiza nome do projeto", description = "Atualiza nome do projeto se passar das regras de negócio")
    @PatchMapping("/atualizar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjetoResponse> atualizarNomeProjeto(@RequestBody @Valid AtualizarNomeProjetoRequest request) {
        return ResponseEntity.ok(projetoMapper.toDTO(projetoService.atualizarNomeProjeto(request.idProjeto(), request.novoNome())));
    }

    @Operation(summary = "Atualiza status do projeto", description = "Atualiza status do projeto se passar das regras de negócio")
    @PatchMapping("/novoStatus")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjetoResponse> atualizarStatusProjeto(@RequestBody @Valid AtualizarStatusProjetoRequest request) {
        return ResponseEntity.ok(projetoMapper.toDTO(projetoService.atualizarStatusProjeto(request.idProjeto(), request.novoStatus())));
    }

    @Operation(summary = "Remove projeto", description = "Remove o cadastro do projeto")
    @DeleteMapping("/{idProjeto}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removerProjeto(@PathVariable UUID idProjeto) {
        projetoService.removerProjeto(idProjeto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Adiciona entrega ao projeto", description = "Cria uma nova entrega associada ao projeto")
    @PostMapping("/entregas")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<EntregaResponse> adicionarEntrega(@RequestBody @Valid EntregaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(entregaMapper.toDTO(projetoService.adicionarEntrega(
                request.idProjeto(),
                entregaMapper.toEntity(request),
                request.idOrganizacaoSolicitante(),
                request.idGrupoPesquisaSolicitante(),
                request.idOrganizacaoSolicitada(),
                request.idGrupoPesquisaSolicitado(),
                request.idUsuarioSolicitante()
        )));
    }

    @Operation(summary = "Lista entregas do projeto", description = "Retorna todas as entregas associadas ao projeto")
    @GetMapping("/entregas/{idProjeto}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<List<EntregaResponse>> buscarEntregasPorProjeto(@PathVariable UUID idProjeto) {
        return ResponseEntity.ok(projetoService.buscarEntregasPorProjeto(idProjeto).stream().map(entregaMapper::toDTO).toList());
    }

    @Operation(summary = "Atualiza entrega", description = "Atualiza título, descrição e prazo de uma entrega")
    @PatchMapping("/entregas")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<EntregaResponse> atualizarEntrega(@RequestBody @Valid AtualizarEntregaRequest request) {
        return ResponseEntity.ok(entregaMapper.toDTO(projetoService.atualizarEntrega(
                request.idEntrega(),
                request.titulo(),
                request.descricao(),
                request.prazoDesejado()
        )));
    }

    @Operation(summary = "Cancela entrega", description = "Altera o status da entrega para CANCELADA")
    @PatchMapping("/entregas/cancelar/{idEntrega}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<EntregaResponse> cancelarEntrega(@PathVariable UUID idEntrega) {
        return ResponseEntity.ok(entregaMapper.toDTO(projetoService.cancelarEntrega(idEntrega)));
    }

    @Operation(summary = "Conclui entrega", description = "Altera o status da entrega de SOLICITADA para EM_ANALISE")
    @PatchMapping("/entregas/concluir/{idEntrega}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<EntregaResponse> concluirEntrega(@PathVariable UUID idEntrega) {
        return ResponseEntity.ok(entregaMapper.toDTO(projetoService.concluirEntrega(idEntrega)));
    }

    @Operation(summary = "Aceita entrega", description = "Altera o status da entrega de ENTREGUE para ACEITA")
    @PatchMapping("/entregas/aceitar/{idEntrega}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ALUNO') or hasRole('PROFESSOR') or hasRole('EXTERNO')")
    public ResponseEntity<EntregaResponse> aceitarEntrega(@PathVariable UUID idEntrega) {
        return ResponseEntity.ok(entregaMapper.toDTO(projetoService.aceitarEntrega(idEntrega)));
    }

    @Operation(summary = "Aprova entrega", description = "Altera o status da entrega de EM_ANALISE para SOLICITADA")
    @PatchMapping("/entregas/aprovar")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSOR')")
    public ResponseEntity<EntregaResponse> aprovarEntrega(@RequestBody @Valid AprovarEntregaRequest request) {
        return ResponseEntity.ok(entregaMapper.toDTO(projetoService.aprovarEntrega(request.idEntrega(), request.idUsuarioAprovador())));
    }
}
