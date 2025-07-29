package com.marketplace.ifba.service;

import com.marketplace.ifba.dto.*;
import com.marketplace.ifba.exception.DadoConflitoException;
import com.marketplace.ifba.exception.DadoNaoEncontradoException;
import com.marketplace.ifba.mapper.UserMapper;
import com.marketplace.ifba.model.Conexao;
import com.marketplace.ifba.model.Instituicao;
import com.marketplace.ifba.model.User;
import com.marketplace.ifba.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final InstituicaoService instituicaoService;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService, AuthenticationManager authenticationManager, InstituicaoService instituicaoService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.instituicaoService = instituicaoService;
    }

    // ---------- LEITURA

    // BUSCA USUÁRIO A PARTIR DO SEU TOKEN DE ACESSO
    public User buscarUsuarioPorToken(String token) {
        String emailLogin = tokenService.validateToken(token);
        User user = userRepository.findAll().stream().filter(userDetails -> userDetails.getEmail().equals(emailLogin)).findFirst().orElse(null);

        if (user == null) {
            throw new DadoNaoEncontradoException("Usuário não encontrado para o token fornecido.");
        }
        return user;
    }

    // BUSCA USUÁRIO PELO SEU ID
    public User buscarUsuarioPorID(UUID idUsuario) {
        return userRepository.findById(idUsuario)
                .orElseThrow(() -> new DadoNaoEncontradoException("Usuário não encontrada com o ID: " + idUsuario));
    }

    // BUSCA USUÁRIO PELO SEU EMAIL
    public User buscarUsuarioPorEmail(String email) {
        return userRepository.findAll().stream().filter(userDetails -> userDetails.getEmail().equals(email)).findFirst().orElse(null);
    }

    // LISTA TODOS OS USUÁRIOS DO SISTEMA
    public List<User> buscarTodosUsuarios() {
        return userRepository.findAll();
    }

    // ---------- ESCRITA

    // REGISTRO DO USUÁRIO
    public User registrarUsuario(User user) {
        if (userRepository.findAll().stream().filter(userDetails -> userDetails.getEmail().equals(user.getEmail())).findFirst() != null) {
            throw new DadoConflitoException("Email já registrado.");
        }
        String encryptedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encryptedPassword);
        user.setDataRegistro(LocalDate.now());

        return userRepository.save(user);
    }

    // ASSOCIA USUÁRIO COM UMA INSTITUIÇÃO
    public void associarInstituicaoUsuario(UUID idUsuario, UUID idInstituicao) {
        User usuario = userRepository.findById(idUsuario)
                .orElseThrow(() -> new DadoNaoEncontradoException("Usuário não encontrada com o ID: " + idUsuario));

        usuario.setInstituicao(instituicaoService.buscarInstituicaoPorId(idInstituicao));
    }

    // RESGISTRA LOGIN DO USUÁRIO
    public String registraLogin(String email, String password) {
        var userNamePassword = new UsernamePasswordAuthenticationToken(email, password);
        var auth = this.authenticationManager.authenticate(userNamePassword);
        User userAutenticado = (User) auth.getPrincipal();

        Conexao newConnection = new Conexao();
        newConnection.setUser_connected(userAutenticado);
        newConnection.setDataConexao(LocalDateTime.now());
        userAutenticado.getConexoes().add(newConnection);
        userRepository.save(userAutenticado);

        return tokenService.generateToken(userAutenticado);
    }

    // ATUALIZA USUÁRIO
    public User atualizarUsuario(UUID idUsuario, User user) {
        User userSaved = userRepository.findById(idUsuario).orElseThrow(() -> new DadoNaoEncontradoException("Usuário não encontrada com o ID: " + idUsuario));

        // ATRIBUTOS QUE PODEM SER ALTERADOS
        userSaved.setNomeCompleto(user.getNomeCompleto());
        userSaved.setTelefone(user.getTelefone());
        userSaved.setBiografia(user.getBiografia());

        return userRepository.save(userSaved);
    }

    // REMOVE USUÁRIO PELO SEU ID
    public void removerUsuario(UUID idUsuario) {
        if (!userRepository.existsById(idUsuario)) {
            throw new EntityNotFoundException("Usuário não encontrado! ID: " + idUsuario);
        }
        userRepository.deleteById(idUsuario);
    }
}
