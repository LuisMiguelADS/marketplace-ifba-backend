package com.marketplace.ifba.controller;

import com.marketplace.ifba.dto.UserRequest;
import com.marketplace.ifba.dto.UserResponse;
import com.marketplace.ifba.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    public ResponseEntity<UserResponse> criar(@RequestBody @Valid UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.salvarUsuario(request));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> listarTodos() {
        List<UserResponse> usuarios = userService.listarUsario();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> buscarPorID(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.buscarPorID(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> atualizar(@PathVariable UUID id, @RequestBody @Valid UserRequest request) {
        return ResponseEntity.ok(userService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable UUID id) {
        userService.removerUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
