package com.marketplace.ifba.controller;

import com.marketplace.ifba.dto.AprovaOuReprovaOfertaSolucaoRequest;
import com.marketplace.ifba.dto.OfertaSolucaoRequest;
import com.marketplace.ifba.dto.OfertaSolucaoResponse;
import com.marketplace.ifba.mapper.OfertaSolucaoMapper;
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
    private final OfertaSolucaoMapper ofertaSolucaoMapper;

    public OfertaSolucaoController(OfertaSolucaoService ofertaSolucaoService, OfertaSolucaoMapper ofertaSolucaoMapper) {
        this.ofertaSolucaoService = ofertaSolucaoService;
        this.ofertaSolucaoMapper = ofertaSolucaoMapper;
    }

    @Operation(summary = "Retorna oferta solução a partir do ID", description = "Procura uma oferta solução salva com o ID informado")
    @GetMapping(params = "idOfertaSolucao")
    public ResponseEntity<OfertaSolucaoResponse> buscarOfertaSolucaoPorId(@RequestParam UUID idOfertaSolucao) {
        return ResponseEntity.ok(ofertaSolucaoMapper.toDTO(ofertaSolucaoService.buscarOfertaSolucaoPorId(idOfertaSolucao)));
    }

    @Operation(summary = "Retorna oferta solução a partir do NOME", description = "Procura uma oferta solução salva com o NOME informado")
    @GetMapping(value = "/nome/", params = "nome")
    public ResponseEntity<OfertaSolucaoResponse> buscarOfertaSolucaoPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(ofertaSolucaoMapper.toDTO(ofertaSolucaoService.buscarOfertasSolucaoPorNome(nome)));
    }

    @Operation(summary = "Retorna oferta solução a partir do STATUS", description = "Procura uma oferta solução salva com o STATUS informado")
    @GetMapping(value = "/status/", params = "status")
    public ResponseEntity<List<OfertaSolucaoResponse>> buscarOfertaSolucaoPorStatus(@RequestParam StatusOfertaSolucao status) {
        return ResponseEntity.ok(ofertaSolucaoService.buscarOfertasSolucaoPorStatus(status).stream().map(ofertaSolucaoMapper::toDTO).toList());
    }

    @Operation(summary = "Retorna todas as ofertas solução", description = "Retorna todas as ofertas solução cadastradas")
    @GetMapping
    public ResponseEntity<List<OfertaSolucaoResponse>> buscarTodasOfertaSolucao() {
        return ResponseEntity.ok(ofertaSolucaoService.buscarTodasOfertasSolucao().stream().map(ofertaSolucaoMapper::toDTO).toList());
    }

    @Operation(summary = "Registra oferta solução", description = "Realiza registro da oferta solução se passar das regras de negócio")
    @PostMapping
    public ResponseEntity<OfertaSolucaoResponse> registrarOfertaSolucao(@RequestBody @Valid OfertaSolucaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ofertaSolucaoMapper.toDTO(ofertaSolucaoService.registrarOfertaSolucao(request.idDemanda(), ofertaSolucaoMapper.toEntity(request))));
    }

    @Operation(summary = "Atualiza oferta solução", description = "Atualiza oferta solução se passar das regras de negócio")
    @PutMapping(params = "idOfertaSolucao")
    public ResponseEntity<OfertaSolucaoResponse> atualizarOfertaSolucao(@RequestBody @Valid OfertaSolucaoRequest request) {
        return ResponseEntity.ok(ofertaSolucaoMapper.toDTO(ofertaSolucaoService.atualizarOfertaSolucao(request.idDemanda(), ofertaSolucaoMapper.toEntity(request))));
    }

    @Operation(summary = "Aprova ou reprova oferta solução", description = "Aprova ou reprova oferta solução")
    @PatchMapping(value = "/aprovar-reprovar")
    public ResponseEntity<OfertaSolucaoResponse> aprovarOfertaSolucao(@RequestParam AprovaOuReprovaOfertaSolucaoRequest request) {
        return ResponseEntity.ok(ofertaSolucaoMapper.toDTO(ofertaSolucaoService.aprovarOuReprovarOfertaSolucao(request.idOfertaSolucao(), request.decisao())));
    }

    @Operation(summary = "Remove oferta solução", description = "Remove o cadastro da oferta solução")
    @DeleteMapping(params = "idOfertaSolucao")
    public ResponseEntity<Void> removerOfertaSolucao(@RequestParam UUID id) {
        ofertaSolucaoService.removerOfertaSolucao(id);
        return ResponseEntity.noContent().build();
    }
}
