package com.marketplace.ifba.service;

import com.marketplace.ifba.exception.*;
import com.marketplace.ifba.model.*;
import com.marketplace.ifba.model.enums.StatusSolicitacao;
import com.marketplace.ifba.repository.InstituicaoRepository;
import com.marketplace.ifba.repository.OrganizacaoRepository;
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
    private final AuthenticationManager authenticationManager;
    private final OrganizacaoRepository organizacaoRepository;
    private final InstituicaoRepository instituicaoRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService, AuthenticationManager authenticationManager, OrganizacaoRepository organizacaoRepository, InstituicaoRepository instituicaoRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.organizacaoRepository = organizacaoRepository;
        this.instituicaoRepository = instituicaoRepository;
    }

    // ---------- LEITURA

    // BUSCA USUÁRIO A PARTIR DO SEU TOKEN DE ACESSO
    public User buscarUsuarioPorToken(String token) {
        String emailLogin = tokenService.validateToken(token);
        User user = userRepository.findAll().stream().filter(userDetails -> userDetails.getEmail().equals(emailLogin)).findFirst().orElse(null);

        if (user == null) {
            throw new UsuarioInvalidoException("Usuário não encontrado para o token fornecido.");
        }
        return user;
    }

    // BUSCA USUÁRIO PELO SEU ID
    public User buscarUsuarioPorID(UUID idUsuario) {
        return userRepository.findById(idUsuario)
                .orElseThrow(() -> new UsuarioInvalidoException("Usuário não encontrada com o ID: " + idUsuario));
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
        if (userRepository.findAll().stream().anyMatch(userDetails -> userDetails.getEmail().equals(user.getEmail()))) {
            throw new EmailInvalidoException("Email já registrado.");
        }
        String encryptedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encryptedPassword);
        user.setDataRegistro(LocalDate.now());

        return userRepository.save(user);
    }

    // ASSOCIA USUÁRIO COM UMA INSTITUIÇÃO
    public void associarInstituicaoUsuario(UUID idUsuario, UUID idInstituicao) {
        User usuario = userRepository.findById(idUsuario)
                .orElseThrow(() -> new UsuarioInvalidoException("Usuário não encontrada com o ID: " + idUsuario));

        Instituicao instituicao = instituicaoRepository.findById(idInstituicao)
                .orElseThrow(() -> new InstituicaoInvalidaException("Instituição não encontrada com o ID: " + idInstituicao));

        // ##################### para desevolver
    }

    // ASSOCIA USUÁRIO COM UMA ORGANIZAÇÃO
    public void associarOrganizacaoUsuario(UUID idUsuario, UUID idOrganizacao) {
        User usuario = userRepository.findById(idUsuario)
                .orElseThrow(() -> new UsuarioInvalidoException("Usuário não encontrada com o ID: " + idUsuario));

        Organizacao organizacao = organizacaoRepository.findById(idOrganizacao)
                .orElseThrow(() -> new UsuarioInvalidoException("Organizacao não encontrada com o ID: " + idOrganizacao));

        if (usuario.getOrganizacao() != null) throw new OrganizacaoInvalidaException("O usuário já está associado há uma organização");

        if (usuario.getInstituicao() != null) throw new InstituicaoInvalidaException("O usuário está associado há uma instituição, sendo assim, não pode se associar uma organização");

        organizacao.getUsuariosIntegrantes().add(usuario);
        organizacao.getSolicitacoes().forEach((solicitacao) -> {
            if (solicitacao.getUserApplicant().equals(usuario)) {
                solicitacao.setStatus(StatusSolicitacao.APROVADA);
            }
        });
        organizacaoRepository.save(organizacao);

        usuario.setOrganizacao(organizacao);
        userRepository.save(usuario);
    }

    // SOLICITA A ASSOIAÇÃO DO USUÁRIO COM UMA ORGANIZAÇÃO
    public void solicitarAssociacaoOrganizacao(UUID idUsuario, UUID idOrganizacao) {
        User usuario = userRepository.findById(idUsuario)
                .orElseThrow(() -> new UsuarioInvalidoException("Usuário não encontrada com o ID: " + idUsuario));

        Organizacao organizacao = organizacaoRepository.findById(idOrganizacao)
                .orElseThrow(() -> new OrganizacaoInvalidaException("Organizacao não encontrada com o ID: " + idOrganizacao));

        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setOrganizacaoRequested(organizacao);
        solicitacao.setUserApplicant(usuario);
        solicitacao.setStatus(StatusSolicitacao.ATIVA);

        organizacao.getSolicitacoes().add(solicitacao);
        organizacaoRepository.save(organizacao);
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
        User userSaved = userRepository.findById(idUsuario).orElseThrow(() -> new UsuarioInvalidoException("Usuário não encontrada com o ID: " + idUsuario));

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
