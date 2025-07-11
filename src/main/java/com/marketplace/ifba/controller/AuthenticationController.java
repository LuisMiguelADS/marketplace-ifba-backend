package com.marketplace.ifba.controller;

import com.marketplace.ifba.dto.AuthenticationDTO;
import com.marketplace.ifba.dto.LoginResponse;
import com.marketplace.ifba.dto.RegisterDTO;
import com.marketplace.ifba.model.User;
import com.marketplace.ifba.repository.UserRepository;
import com.marketplace.ifba.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        var userNamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(userNamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data) {
        if (this.userRepository.findByEmail(data.email()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        var newUser = new User(data.nomeCompleto(), data.role(), data.email(), data.telefone(), encryptedPassword, data.cpf(),
                data.dataNascimento(), data.biografia(), data.fotoPerfilURL(), data.endereco(), data.instituicao(), data.organizacao());
        newUser.setDataRegistro(LocalDateTime.now());

        this.userRepository.save(newUser);

        return ResponseEntity.ok().build();
    }
}
