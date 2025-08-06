package com.marketplace.ifba.controller;

import com.marketplace.ifba.dto.AssociarUsuarioInstituicaoOrganizacao;
import com.marketplace.ifba.dto.UserRequest;
import com.marketplace.ifba.dto.UserResponse;
import com.marketplace.ifba.mapper.UserMapper;
import com.marketplace.ifba.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuários", description = "Gerencia as operações relacionadas os usuários no marketplace.")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Operation(summary = "Retorna usuário a partir do TOKEN", description = "Procura um usuário com o TOKEN informado")
    @GetMapping("/token/{token}")
    public ResponseEntity<UserResponse> buscarUsuarioPorToken(@PathVariable String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userMapper.toDTO(userService.buscarUsuarioPorToken(token)));
    }

    @Operation(summary = "Retorna usuário a partir do ID", description = "Procura um usuário com o ID informado")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<UserResponse> buscarUsuarioPorID(@PathVariable UUID id) {
        return ResponseEntity.ok(userMapper.toDTO(userService.buscarUsuarioPorID(id)));
    }

    @Operation(summary = "Retorna todos os usuários", description = "Retorna todos os usuários cadastrados")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<UserResponse>> listarTodosUsuarios() {
        List<UserResponse> usuarios = userService.buscarTodosUsuarios().stream().map(userMapper::toDTO).toList();
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping("/associar-instituicao")
    public ResponseEntity<Void> associarInstituicaoUsuario(@RequestBody @Valid AssociarUsuarioInstituicaoOrganizacao request) {
        userService.associarInstituicaoUsuario(request.idUsuario(), request.idInstituicaoOuOrganizacao());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/associar-organizacao")
    public ResponseEntity<Void> associarOrganizacaoUsuario(@RequestBody @Valid AssociarUsuarioInstituicaoOrganizacao request) {
        userService.associarOrganizacaoUsuario(request.idUsuario(), request.idInstituicaoOuOrganizacao());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/solicitar-organizacao")
    public ResponseEntity<Void> solicitarAssociacaoOrganizacao(@RequestBody @Valid AssociarUsuarioInstituicaoOrganizacao request) {
        userService.solicitarAssociacaoOrganizacao(request.idUsuario(), request.idInstituicaoOuOrganizacao());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualiza usuário", description = "Realiza registro do usuário se passar das regras de negócio")
    @PutMapping("/{idUsuario}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<UserResponse> atualizarUsuario(@PathVariable UUID idUsuario, @RequestBody @Valid UserRequest request) {
        return ResponseEntity.ok(userMapper.toDTO(userService.atualizarUsuario(idUsuario, userMapper.toEntity(request))));
    }

    @Operation(summary = "Remove usuário", description = "Remove o cadastro do usuário")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removerUsuario(@PathVariable UUID id) {
        userService.removerUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
