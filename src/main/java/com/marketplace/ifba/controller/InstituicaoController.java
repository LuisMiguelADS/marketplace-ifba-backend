package com.marketplace.ifba.controller;

import com.marketplace.ifba.dto.AprovarInstituicaoRequest;
import com.marketplace.ifba.dto.InstituicaoRequest;
import com.marketplace.ifba.dto.InstituicaoResponse;
import com.marketplace.ifba.service.InstituicaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/instituicoes")
public class InstituicaoController {

    @Autowired
    private final InstituicaoService instituicaoService;

    public InstituicaoController(InstituicaoService instituicaoService) {
        this.instituicaoService = instituicaoService;
    }

    @GetMapping("/{idInstituicao}")
    public ResponseEntity<InstituicaoResponse> buscarInstituicaoPorId(@PathVariable UUID idInstituicao) {
        return ResponseEntity.ok(instituicaoService.buscarInstituicaoPorId(idInstituicao));
    }

    @GetMapping("/name/{nome}")
    public ResponseEntity<InstituicaoResponse> buscarInstituicaoPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(instituicaoService.buscarInstituicaoPorNome(nome));
    }

    @GetMapping
    public ResponseEntity<List<InstituicaoResponse>> buscarInstituicoes() {
        return ResponseEntity.ok(instituicaoService.buscarTodasInstituicoes());
    }

    @PostMapping("/{idUsuarioRegistrador}")
    public ResponseEntity<InstituicaoResponse> registrarInstituicao(@RequestBody @Valid InstituicaoRequest request, @PathVariable UUID idUsuarioRegistrador) {
        return ResponseEntity.status(HttpStatus.CREATED).body(instituicaoService.registrarInstituicao(request, idUsuarioRegistrador));
    }

    @PutMapping("/{idInstituicao}")
    public ResponseEntity<InstituicaoResponse> atualizarInstituicao(@RequestBody @Valid InstituicaoRequest request, @PathVariable UUID idInstituicao) {
        return ResponseEntity.ok(instituicaoService.atualizarInstituicao(request, idInstituicao));
    }

    @PostMapping("/aprovar")
    public ResponseEntity<InstituicaoResponse> aprovarInstituicao(@RequestBody @Valid AprovarInstituicaoRequest request) {
        return ResponseEntity.ok(instituicaoService.aprovarInstituicao(request.idInstituicao(), request.idAdmAprovador()));
    }

    @DeleteMapping("/{idInstituicao}")
    public ResponseEntity<Void> deletarInstituicao(@PathVariable UUID idInstituicao) {
        instituicaoService.deletarInstituicao(idInstituicao);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
