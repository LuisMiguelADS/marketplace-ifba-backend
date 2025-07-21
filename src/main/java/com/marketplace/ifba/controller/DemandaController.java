package com.marketplace.ifba.controller;

import com.marketplace.ifba.dto.AprovarDemandaRequest;
import com.marketplace.ifba.dto.AtualizarStatusDemandaRequest;
import com.marketplace.ifba.dto.DemandaRequest;
import com.marketplace.ifba.dto.DemandaResponse;
import com.marketplace.ifba.service.DemandaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/demandas")
public class DemandaController {

    private final DemandaService demandaService;

    public DemandaController(DemandaService demandaService) {
        this.demandaService = demandaService;
    }

    @GetMapping("/{idDemanda}")
    public ResponseEntity<DemandaResponse> buscarDemandaPorId(@PathVariable UUID idDemanda) {
        demandaService.incrementarVizualizacao(idDemanda);
        return ResponseEntity.ok(demandaService.buscarDemandaPorId(idDemanda));
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<DemandaResponse> buscarDemandaPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(demandaService.buscarDemandaPorNome(nome));
    }

    @GetMapping("/organizacao/{idOrganizacao}")
    public ResponseEntity<List<DemandaResponse>> buscarDemandasPorOrganizacao(@PathVariable UUID idOrganizacao) {
        return ResponseEntity.ok(demandaService.buscarDemandasPorOrganizacao(idOrganizacao));
    }

    @GetMapping
    public ResponseEntity<List<DemandaResponse>> buscarTodasDemandas() {
        return ResponseEntity.ok(demandaService.buscarTodasDemandas());
    }

    @PostMapping
    public ResponseEntity<DemandaResponse> registrarDemanda(@RequestBody @Valid DemandaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(demandaService.registrarDemanda(request));
    }

    @PutMapping("/{idDemanda}")
    public ResponseEntity<DemandaResponse> atualizarDemanda(@RequestBody @Valid DemandaRequest request, @PathVariable UUID idDemanda) {
        return ResponseEntity.ok(demandaService.atualizarDemanda(idDemanda, request));
    }

    @PatchMapping("/{idDemanda}/status")
    public ResponseEntity<DemandaResponse> atualizarStatusDemanda(@PathVariable UUID idDemanda, @RequestBody @Valid AtualizarStatusDemandaRequest request) {
        return ResponseEntity.ok(demandaService.atualizarStatusDemanda(idDemanda, request.novoStatus()));
    }

    @PatchMapping("/aprovar")
    public ResponseEntity<DemandaResponse> aprovarDemandaDemandante(@RequestBody @Valid AprovarDemandaRequest request) {
        return ResponseEntity.ok(demandaService.aprovarDemandaDemandante(request.idDemanda(), request.aprovado()));
    }

    @DeleteMapping("/{idDemanda}")
    public ResponseEntity<Void> removerDemanda(@PathVariable UUID idDemanda) {
        demandaService.removerDemanda(idDemanda);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
