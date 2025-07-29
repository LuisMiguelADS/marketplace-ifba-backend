package com.marketplace.ifba.controller;

import com.marketplace.ifba.dto.AuthenticationDTO;
import com.marketplace.ifba.dto.LoginResponse;
import com.marketplace.ifba.dto.UserRequest;
import com.marketplace.ifba.dto.UserResponse;
import com.marketplace.ifba.mapper.UserMapper;
import com.marketplace.ifba.model.User;
import com.marketplace.ifba.repository.UserRepository;
import com.marketplace.ifba.service.TokenService;
import com.marketplace.ifba.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação do Usuário", description = "Gerencia a autenticação da API")
public class AuthenticationController {

    private final UserService userService;
    private final UserMapper userMapper;

    public AuthenticationController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Operation(summary = "Login do usuário", description = "Realiza login do usuário, retornando as informações e token de autenticação do mesmo.")
    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid AuthenticationDTO data) {
        UserResponse user = userMapper.toDTO(userService.buscarUsuarioPorEmail(data.email()));
        String token = userService.registraLogin(data.email(), data.password());
        return ResponseEntity.ok(new LoginResponse(token, user));
    }

    @Operation(summary = "Registra usuário", description = "Realiza registro do usuário se passar das regras de negócio, como CPF único no sistema")
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserRequest request) {
        return ResponseEntity.ok(userMapper.toDTO(userService.registrarUsuario(userMapper.toEntity(request))));
    }
}
