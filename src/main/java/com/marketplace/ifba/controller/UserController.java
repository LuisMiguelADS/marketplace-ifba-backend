package com.marketplace.ifba.controller;

import com.marketplace.ifba.dto.UserRequest;
import com.marketplace.ifba.dto.UserResponse;
import com.marketplace.ifba.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ROTA PARA BUSCAR UM USUÁRIO PELO SEU TOKEN DE ACESSO
    @GetMapping("/token/{token}")
    public ResponseEntity<UserResponse> buscarUsuarioPorToken(@PathVariable String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userService.buscarUsuarioPorToken(token));
    }

    // ROTA PARA LISTAR TODOS OS USUÁRIOS DO SISTEMA
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<UserResponse>> listarTodosUsuarios() {
        List<UserResponse> usuarios = userService.buscarTodosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    // ROTA PARA BUSCAR USUÁRIO PELO SEU ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<UserResponse> buscarUsuarioPorID(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.buscarUsuarioPorID(id));
    }

    // ROTA PARA ATUALIZAR USUÁRIO
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<UserResponse> atualizarUsuario(@PathVariable UUID id, @RequestBody @Valid UserRequest request) {
        return ResponseEntity.ok(userService.atualizarUsuario(id, request));
    }

    // ROTA PARA REMOVER USUÁRIO PELO SEU ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removerUsuario(@PathVariable UUID id) {
        userService.removerUsuario(id);

        return ResponseEntity.noContent().build();
    }
}
