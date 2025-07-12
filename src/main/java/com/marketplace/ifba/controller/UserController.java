package com.marketplace.ifba.controller;

import com.marketplace.ifba.dto.UserRequest;
import com.marketplace.ifba.dto.UserResponse;
import com.marketplace.ifba.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<List<UserResponse>> listarTodosUsuarios() {
        List<UserResponse> usuarios = userService.listarUsario();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> buscarUsuarioPorID(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.buscarPorID(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> atualizarUsuario(@PathVariable UUID id, @RequestBody @Valid UserRequest request) {
        return ResponseEntity.ok(userService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerUsuario(@PathVariable UUID id) {
        userService.removerUsuario(id);

        return ResponseEntity.noContent().build();
    }
}
