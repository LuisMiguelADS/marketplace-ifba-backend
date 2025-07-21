package com.marketplace.ifba.controller;

import com.marketplace.ifba.dto.PropostaRequest;
import com.marketplace.ifba.dto.PropostaResponse;
import com.marketplace.ifba.model.enums.StatusProposta;
import com.marketplace.ifba.service.PropostaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

    private final PropostaService propostaService;

    public PropostaController(PropostaService propostaService) {
        this.propostaService = propostaService;
    }

    @GetMapping("/{idProposta}")
    public ResponseEntity<PropostaResponse> buscarPropostaPorId(@PathVariable UUID idProposta) {
        return ResponseEntity.ok(propostaService.buscarPropostaPorId(idProposta));
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<PropostaResponse> buscarPropostaPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(propostaService.buscarPropostaPorNome(nome));
    }

    @GetMapping("/grupo-pesquisa/{idGrupoPesquisa}")
    public ResponseEntity<List<PropostaResponse>> buscarPropostasPorGrupoPesquisa(@PathVariable UUID idGrupoPesquisa) {
        return ResponseEntity.ok(propostaService.buscarPropostasPorGrupoPesquisa(idGrupoPesquisa));
    }

    @GetMapping("/instituicao/{idInstituicao}")
    public ResponseEntity<List<PropostaResponse>> buscarPropostasPorInstituicao(@PathVariable UUID idInstituicao) {
        return ResponseEntity.ok(propostaService.buscarPropostasPorInstituicao(idInstituicao));
    }

    @GetMapping
    public ResponseEntity<List<PropostaResponse>> buscarTodasPropostas() {
        return ResponseEntity.ok(propostaService.buscarTodasPropostas());
    }

    @PostMapping
    public ResponseEntity<PropostaResponse> registrarProposta(@RequestBody @Valid PropostaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(propostaService.registrarProposta(request));
    }

    @PutMapping("/{idProposta}")
    public ResponseEntity<PropostaResponse> atualizarProposta(@RequestBody @Valid PropostaRequest request, @PathVariable UUID idProposta) {
        return ResponseEntity.ok(propostaService.atualizarProposta(idProposta, request));
    }

    @PatchMapping("/{idProposta}/status")
    public ResponseEntity<PropostaResponse> atualizarStatusProposta(@PathVariable UUID idProposta, @RequestParam StatusProposta novoStatus) {
        return ResponseEntity.ok(propostaService.atualizarStatusProposta(idProposta, novoStatus));
    }

    @DeleteMapping("/{idProposta}")
    public ResponseEntity<Void> removerProposta(@PathVariable UUID idProposta) {
        propostaService.removerProposta(idProposta);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
