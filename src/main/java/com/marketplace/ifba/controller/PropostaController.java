package com.marketplace.ifba.controller;

import com.marketplace.ifba.dto.PropostaRequest;
import com.marketplace.ifba.dto.PropostaResponse;
import com.marketplace.ifba.model.enums.StatusProposta;
import com.marketplace.ifba.service.PropostaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/propostas")
@Tag(name = "Proposta", description = "Gerencia as operações relacionadas às propostas no marketplace.")
public class PropostaController {

    private final PropostaService propostaService;

    public PropostaController(PropostaService propostaService) {
        this.propostaService = propostaService;
    }

    @Operation(summary = "Retorna proposta a partir do ID", description = "Procura uma proposta com o ID informado")
    @GetMapping("/{idProposta}")
    public ResponseEntity<PropostaResponse> buscarPropostaPorId(@PathVariable UUID idProposta) {
        return ResponseEntity.ok(propostaService.buscarPropostaPorId(idProposta));
    }

    @Operation(summary = "Retorna proposta a partir do NOME", description = "Procura uma proposta salva com o NOME informado")
    @GetMapping("/nome/{nome}")
    public ResponseEntity<PropostaResponse> buscarPropostaPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(propostaService.buscarPropostaPorNome(nome));
    }

    @Operation(summary = "Retorna proposta a partir do GRUPO PESQUISA associado", description = "Procura uma proposta salva com o GRUPO PESQUISA associado")
    @GetMapping("/grupo-pesquisa/{idGrupoPesquisa}")
    public ResponseEntity<List<PropostaResponse>> buscarPropostasPorGrupoPesquisa(@PathVariable UUID idGrupoPesquisa) {
        return ResponseEntity.ok(propostaService.buscarPropostasPorGrupoPesquisa(idGrupoPesquisa));
    }

    @Operation(summary = "Retorna proposta a partir da INSTITUIÇÃO associado", description = "Procura uma proposta salva com a INSTITUIÇÃO associada")
    @GetMapping("/instituicao/{idInstituicao}")
    public ResponseEntity<List<PropostaResponse>> buscarPropostasPorInstituicao(@PathVariable UUID idInstituicao) {
        return ResponseEntity.ok(propostaService.buscarPropostasPorInstituicao(idInstituicao));
    }

    @Operation(summary = "Retorna todas as propostas", description = "Retorna todas as propostas cadastradas")
    @GetMapping
    public ResponseEntity<List<PropostaResponse>> buscarTodasPropostas() {
        return ResponseEntity.ok(propostaService.buscarTodasPropostas());
    }

    @Operation(summary = "Registra proposta", description = "Realiza registro da proposta se passar das regras de negócio")
    @PostMapping
    public ResponseEntity<PropostaResponse> registrarProposta(@RequestBody @Valid PropostaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(propostaService.registrarProposta(request));
    }

    @Operation(summary = "Atualiza proposta", description = "Realiza registro da proposta se passar das regras de negócio")
    @PutMapping("/{idProposta}")
    public ResponseEntity<PropostaResponse> atualizarProposta(@RequestBody @Valid PropostaRequest request, @PathVariable UUID idProposta) {
        return ResponseEntity.ok(propostaService.atualizarProposta(idProposta, request));
    }

    @Operation(summary = "Atualiza status da proposta", description = "Atualiza status da proposta se passar das regras de negócio")
    @PatchMapping("/{idProposta}/status")
    public ResponseEntity<PropostaResponse> atualizarStatusProposta(@PathVariable UUID idProposta, @RequestParam StatusProposta novoStatus) {
        return ResponseEntity.ok(propostaService.atualizarStatusProposta(idProposta, novoStatus));
    }

    @Operation(summary = "Remove proposta", description = "Remove o cadastro da proposta")
    @DeleteMapping("/{idProposta}")
    public ResponseEntity<Void> removerProposta(@PathVariable UUID idProposta) {
        propostaService.removerProposta(idProposta);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
