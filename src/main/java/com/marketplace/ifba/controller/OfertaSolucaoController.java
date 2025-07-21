package com.marketplace.ifba.controller;

import com.marketplace.ifba.dto.OfertaSolucaoRequest;
import com.marketplace.ifba.dto.OfertaSolucaoResponse;
import com.marketplace.ifba.model.enums.StatusOfertaSolucao;
import com.marketplace.ifba.service.OfertaSolucaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ofertas-solucao")
public class OfertaSolucaoController {

    @Autowired
    private final OfertaSolucaoService ofertaSolucaoService;

    public OfertaSolucaoController(OfertaSolucaoService ofertaSolucaoService) {
        this.ofertaSolucaoService = ofertaSolucaoService;
    }

    @GetMapping(params = "idOfertaSolucao")
    public ResponseEntity<OfertaSolucaoResponse> buscarOfertaSolucaoPorId(@RequestParam UUID idOfertaSolucao) {
        OfertaSolucaoResponse response = ofertaSolucaoService.buscarOfertaSolucaoPorId(idOfertaSolucao);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<OfertaSolucaoResponse>> buscarOfertaSolucaoTodos() {
        List<OfertaSolucaoResponse> response = ofertaSolucaoService.buscarTodasOfertasSolucao();
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/nome/", params = "nome")
    public ResponseEntity<List<OfertaSolucaoResponse>> buscarOfertaSolucaoPorNome(@RequestParam String nome) {
        List<OfertaSolucaoResponse> response = ofertaSolucaoService.buscarOfertasSolucaoPorNome(nome);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/status/", params = "status")
    public ResponseEntity<List<OfertaSolucaoResponse>> buscarOfertaSolucaoPorStatus(@RequestParam String status) {
        List<OfertaSolucaoResponse> response = ofertaSolucaoService.buscarOfertasSolucaoPorStatus(StatusOfertaSolucao.fromString(status));
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/aprovadas/", params = "aprovada")
    public ResponseEntity<List<OfertaSolucaoResponse>> buscarOfertaSolucaoAprovadas(@RequestParam Boolean aprovado) {
        List<OfertaSolucaoResponse> response = ofertaSolucaoService.buscarOfertasSolucaoAprovadas(aprovado);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<OfertaSolucaoResponse> registrarOfertaSolucao(@RequestBody @Valid OfertaSolucaoRequest request) {
        OfertaSolucaoResponse response = ofertaSolucaoService.registrarOfertaSolucao(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(params = "idOfertaSolucao")
    public ResponseEntity<OfertaSolucaoResponse> atualizarOfertaSolucao(@RequestBody @Valid OfertaSolucaoRequest request, @PathVariable UUID idOfertaSolucao) {
        OfertaSolucaoResponse response = ofertaSolucaoService.atualizarOfertaSolucao(idOfertaSolucao, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/aprovar/", params = "idOfertaSolucao")
    public ResponseEntity<OfertaSolucaoResponse> aprovarOfertaSolucao(@RequestParam UUID idOfertaSolucao) {
        OfertaSolucaoResponse response = ofertaSolucaoService.aprovarOfertaSolucao(idOfertaSolucao);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/reprovar/", params = "idOfertaSolucao")
    public ResponseEntity<OfertaSolucaoResponse> reprovarOfertaSolucao(@RequestParam UUID idOfertaSolucao) {
        OfertaSolucaoResponse response = ofertaSolucaoService.reprovarOfertaSolucao(idOfertaSolucao);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(params = "idOfertaSolucao")
    public ResponseEntity<Void> removerOfertaSolucao(@RequestParam UUID id) {
        ofertaSolucaoService.removerOfertaSolucao(id);
        return ResponseEntity.noContent().build();
    }
}
