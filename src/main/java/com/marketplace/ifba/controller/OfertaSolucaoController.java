package com.marketplace.ifba.controller;

import com.marketplace.ifba.dto.OfertaSolucaoRequest;
import com.marketplace.ifba.dto.OfertaSolucaoResponse;
import com.marketplace.ifba.model.enums.StatusOfertaSolucao;
import com.marketplace.ifba.service.OfertaSolucaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ofertas-solucao")
@Tag(name = "Oferta Solução", description = "Gerencia as operações relacionadas às ofertas solução no marketplace.")
public class OfertaSolucaoController {

    private final OfertaSolucaoService ofertaSolucaoService;

    public OfertaSolucaoController(OfertaSolucaoService ofertaSolucaoService) {
        this.ofertaSolucaoService = ofertaSolucaoService;
    }

    @Operation(summary = "Retorna oferta solução a partir do ID", description = "Procura uma oferta solução salva com o ID informado")
    @GetMapping(params = "idOfertaSolucao")
    public ResponseEntity<OfertaSolucaoResponse> buscarOfertaSolucaoPorId(@RequestParam UUID idOfertaSolucao) {
        OfertaSolucaoResponse response = ofertaSolucaoService.buscarOfertaSolucaoPorId(idOfertaSolucao);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Retorna oferta solução a partir do NOME", description = "Procura uma oferta solução salva com o NOME informado")
    @GetMapping(value = "/nome/", params = "nome")
    public ResponseEntity<List<OfertaSolucaoResponse>> buscarOfertaSolucaoPorNome(@RequestParam String nome) {
        List<OfertaSolucaoResponse> response = ofertaSolucaoService.buscarOfertasSolucaoPorNome(nome);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Retorna oferta solução a partir do STATUS", description = "Procura uma oferta solução salva com o STATUS informado")
    @GetMapping(value = "/status/", params = "status")
    public ResponseEntity<List<OfertaSolucaoResponse>> buscarOfertaSolucaoPorStatus(@RequestParam String status) {
        List<OfertaSolucaoResponse> response = ofertaSolucaoService.buscarOfertasSolucaoPorStatus(StatusOfertaSolucao.fromString(status));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Retorna oferta solução APROVADA", description = "Procura uma oferta solução salva APROVADA")
    @GetMapping(value = "/aprovadas/", params = "aprovada")
    public ResponseEntity<List<OfertaSolucaoResponse>> buscarOfertaSolucaoAprovadas(@RequestParam Boolean aprovado) {
        List<OfertaSolucaoResponse> response = ofertaSolucaoService.buscarOfertasSolucaoAprovadas(aprovado);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Retorna todas as ofertas solução", description = "Retorna todas as ofertas solução cadastradas")
    @GetMapping
    public ResponseEntity<List<OfertaSolucaoResponse>> buscarTodasOfertaSolucao() {
        List<OfertaSolucaoResponse> response = ofertaSolucaoService.buscarTodasOfertasSolucao();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Registra oferta solução", description = "Realiza registro da oferta solução se passar das regras de negócio")
    @PostMapping
    public ResponseEntity<OfertaSolucaoResponse> registrarOfertaSolucao(@RequestBody @Valid OfertaSolucaoRequest request) {
        OfertaSolucaoResponse response = ofertaSolucaoService.registrarOfertaSolucao(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Atualiza oferta solução", description = "Atualiza oferta solução se passar das regras de negócio")
    @PutMapping(params = "idOfertaSolucao")
    public ResponseEntity<OfertaSolucaoResponse> atualizarOfertaSolucao(@RequestBody @Valid OfertaSolucaoRequest request, @PathVariable UUID idOfertaSolucao) {
        OfertaSolucaoResponse response = ofertaSolucaoService.atualizarOfertaSolucao(idOfertaSolucao, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Aprova oferta solução", description = "Aprova oferta solução")
    @PatchMapping(value = "/aprovar/", params = "idOfertaSolucao")
    public ResponseEntity<OfertaSolucaoResponse> aprovarOfertaSolucao(@RequestParam UUID idOfertaSolucao) {
        OfertaSolucaoResponse response = ofertaSolucaoService.aprovarOfertaSolucao(idOfertaSolucao);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Reprova oferta solução", description = "Reprova oferta solução")
    @PatchMapping(value = "/reprovar/", params = "idOfertaSolucao")
    public ResponseEntity<OfertaSolucaoResponse> reprovarOfertaSolucao(@RequestParam UUID idOfertaSolucao) {
        OfertaSolucaoResponse response = ofertaSolucaoService.reprovarOfertaSolucao(idOfertaSolucao);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Remove oferta solução", description = "Remove o cadastro da oferta solução")
    @DeleteMapping(params = "idOfertaSolucao")
    public ResponseEntity<Void> removerOfertaSolucao(@RequestParam UUID id) {
        ofertaSolucaoService.removerOfertaSolucao(id);
        return ResponseEntity.noContent().build();
    }
}
