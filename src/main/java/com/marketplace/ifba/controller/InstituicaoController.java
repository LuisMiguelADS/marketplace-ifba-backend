package com.marketplace.ifba.controller;

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

    @GetMapping(params = "idInstituicao")
    public ResponseEntity<InstituicaoResponse> buscarInstituicaoPorId(@RequestParam UUID idInstituicao) {
        return ResponseEntity.ok(instituicaoService.buscarInstituicaoPorId(idInstituicao));
    }

    @GetMapping(params = "nome")
    public ResponseEntity<InstituicaoResponse> buscarInstituicaoPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(instituicaoService.buscarInstituicaoPorNome(nome));
    }

    @GetMapping
    public ResponseEntity<List<InstituicaoResponse>> buscarInstituicoes() {
        return ResponseEntity.ok(instituicaoService.buscarTodasInstituicoes());
    }

    @PostMapping(params = "idUsuarioRegistrador")
    public ResponseEntity<InstituicaoResponse> registrarInstituicao(@RequestBody @Valid InstituicaoRequest request, @RequestParam UUID idUsuarioRegistrador) {
        return ResponseEntity.status(HttpStatus.CREATED).body(instituicaoService.registrarInstituicao(request, idUsuarioRegistrador));
    }

    @PutMapping(params = "idInstituicao")
    public ResponseEntity<InstituicaoResponse> atualizarInstituicao(@RequestBody @Valid InstituicaoRequest request, @RequestParam UUID idInstituicao) {
        return ResponseEntity.ok(instituicaoService.atualizarInstituicao(request, idInstituicao));
    }

    @PostMapping(value = "/aprovar/", params = {"idInstituicao", "idAdmAprovador"})
    public ResponseEntity<InstituicaoResponse> aprovarInstituicao(@RequestParam UUID idInstituicao, @RequestParam UUID idAdmAprovador) {
        return ResponseEntity.ok(instituicaoService.aprovarInstituicao(idInstituicao, idAdmAprovador));
    }

    @DeleteMapping(params = "idInstituicao")
    public ResponseEntity<Void> deletarInstituicao(@RequestParam UUID idInstituicao) {
        instituicaoService.deletarInstituicao(idInstituicao);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
