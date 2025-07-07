package com.marketplace.ifba.controller;

import com.marketplace.ifba.dto.UsuarioRequest;
import com.marketplace.ifba.dto.UsuarioResponse;
import com.marketplace.ifba.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> criar(@RequestBody @Valid UsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.salvarUsuario(request));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        List<UsuarioResponse> usuarios = usuarioService.listarUsario();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorID(@PathVariable UUID id) {
        return ResponseEntity.ok(usuarioService.buscarPorID(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable UUID id, @RequestBody @Valid UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable UUID id) {
        usuarioService.removerUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
