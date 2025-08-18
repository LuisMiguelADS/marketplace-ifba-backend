package com.marketplace.ifba.service;

import com.marketplace.ifba.exception.*;
import com.marketplace.ifba.model.*;
import com.marketplace.ifba.model.enums.StatusSolicitacao;
import com.marketplace.ifba.repository.GrupoPesquisaRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final OrganizacaoRepository organizacaoRepository;
    private final InstituicaoRepository instituicaoRepository;
    private final GrupoPesquisaRepository grupoPesquisaRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService, AuthenticationManager authenticationManager, OrganizacaoRepository organizacaoRepository, InstituicaoRepository instituicaoRepository, GrupoPesquisaRepository grupoPesquisaRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.organizacaoRepository = organizacaoRepository;
        this.instituicaoRepository = instituicaoRepository;
        this.grupoPesquisaRepository = grupoPesquisaRepository;
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

        if (userRepository.findAll().stream().anyMatch(userDetails -> userDetails.getCpf().equals(user.getCpf()))) {
            throw new UsuarioInvalidoException("CPF já registrado.");
        }

        String encryptedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encryptedPassword);
        user.setDataRegistro(LocalDate.now());

        return userRepository.save(user);
    }

    // ASSOCIA USUÁRIO COM UMA INSTITUIÇÃO
    public void associarInstituicaoUsuario(UUID idUsuario, UUID idInstituicao, Boolean decisao) {
        User usuario = userRepository.findById(idUsuario)
                .orElseThrow(() -> new UsuarioInvalidoException("Usuário não encontrada com o ID"));

        Instituicao instituicao = instituicaoRepository.findById(idInstituicao)
                .orElseThrow(() -> new InstituicaoInvalidaException("Instituição não encontrada com o ID"));

        if (usuario.getOrganizacao() != null) throw new InstituicaoInvalidaException("O usuário já está associado há uma instituição");

        if (usuario.getInstituicao() != null) throw new OrganizacaoInvalidaException("O usuário está associado há uma organização, sendo assim, não pode se associar em uma instituição");

        if (decisao) {
            instituicao.getUsuariosIntegrantes().add(usuario);
            instituicao.getSolicitacoes().forEach((solicitacao) -> {
                if (solicitacao.getUserApplicant().equals(usuario)) {
                    solicitacao.setStatus(StatusSolicitacao.APROVADA);
                }
            });
            usuario.setInstituicao(instituicao);
        } else {
            instituicao.getSolicitacoes().forEach((solicitacao) -> {
                if (solicitacao.getUserApplicant().equals(usuario)) {
                    solicitacao.setStatus(StatusSolicitacao.REJEITADA);
                }
            });
        }

        instituicaoRepository.save(instituicao);
        userRepository.save(usuario);
    }

    // ASSOCIA USUÁRIO COM UMA ORGANIZAÇÃO
    public void associarOrganizacaoUsuario(UUID idUsuario, UUID idOrganizacao, Boolean decisao) {
        User usuario = userRepository.findById(idUsuario)
                .orElseThrow(() -> new UsuarioInvalidoException("Usuário não encontrada com o ID"));

        Organizacao organizacao = organizacaoRepository.findById(idOrganizacao)
                .orElseThrow(() -> new UsuarioInvalidoException("Organização não encontrada com o ID"));

        if (usuario.getOrganizacao() != null) throw new OrganizacaoInvalidaException("O usuário já está associado há uma organização");

        if (usuario.getInstituicao() != null) throw new InstituicaoInvalidaException("O usuário está associado há uma instituição, sendo assim, não pode se associar em uma organização");

        if (decisao) {
            organizacao.getUsuariosIntegrantes().add(usuario);
            organizacao.getSolicitacoes().forEach((solicitacao) -> {
                if (solicitacao.getUserApplicant().equals(usuario)) {
                    solicitacao.setStatus(StatusSolicitacao.APROVADA);
                }
            });
            usuario.setOrganizacao(organizacao);
        } else {
            organizacao.getSolicitacoes().forEach((solicitacao) -> {
                if (solicitacao.getUserApplicant().equals(usuario)) {
                    solicitacao.setStatus(StatusSolicitacao.REJEITADA);
                }
            });
        }

        organizacaoRepository.save(organizacao);
        userRepository.save(usuario);
    }

    // ASSOCIA USUÁRIO COM UM GRUPO PESQUISA
    public void associarGrupoPesquisaUsuario(UUID idUsuario, UUID idGrupoPesquisa, Boolean decisao) {
        User usuario = userRepository.findById(idUsuario)
                .orElseThrow(() -> new UsuarioInvalidoException("Usuário não encontrada com o ID"));

        GrupoPesquisa grupoPesquisa = grupoPesquisaRepository.findById(idGrupoPesquisa)
                .orElseThrow(() -> new UsuarioInvalidoException("Grupo pesquisa não encontrada com o ID"));

        if (usuario.getOrganizacao() != null) throw new OrganizacaoInvalidaException("O usuário está associado há uma organização, não pode se associar com um grupo");

        if (usuario.getGrupoPesquisa() != null) throw new GrupoPesquisaInvalidoException("O usuário está associado há um grupo de pesquisa, sendo assim, não pode se associar em outro grupo");

        if (decisao) {
            grupoPesquisa.getUsuarios().add(usuario);
            grupoPesquisa.getSolicitacoes().forEach((solicitacao) -> {
                if (solicitacao.getUserApplicant().equals(usuario)) {
                    solicitacao.setStatus(StatusSolicitacao.APROVADA);
                }
            });
            usuario.setGrupoPesquisa(grupoPesquisa);
        } else {
            grupoPesquisa.getSolicitacoes().forEach((solicitacao) -> {
                if (solicitacao.getUserApplicant().equals(usuario)) {
                    solicitacao.setStatus(StatusSolicitacao.REJEITADA);
                }
            });
        }

        grupoPesquisaRepository.save(grupoPesquisa);
        userRepository.save(usuario);
    }

    // SOLICITA A ASSOCIAÇÃO DO USUÁRIO COM UMA ORGANIZAÇÃO
    public void solicitarAssociacaoOrganizacao(UUID idUsuario, UUID idOrganizacao) {
        User usuario = userRepository.findById(idUsuario)
                .orElseThrow(() -> new UsuarioInvalidoException("Usuário não encontrada com o ID"));

        Organizacao organizacao = organizacaoRepository.findById(idOrganizacao)
                .orElseThrow(() -> new OrganizacaoInvalidaException("Organizacao não encontrada com o ID"));

        if (organizacao.getSolicitacoes().stream().anyMatch(solicitacao -> solicitacao.getUserApplicant().equals(usuario) && solicitacao.getStatus() == StatusSolicitacao.ATIVA)) {
            throw new OrganizacaoInvalidaException("Esse usuário já solicitou a entrada nessa organização.");
        }

        if (usuario.getOrganizacao() != null) {
            throw new OrganizacaoInvalidaException("O usuário já está presente em uma organização");
        }

        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setOrganizacaoRequested(organizacao);
        solicitacao.setUserApplicant(usuario);
        solicitacao.setStatus(StatusSolicitacao.ATIVA);

        organizacao.getSolicitacoes().add(solicitacao);
        organizacaoRepository.save(organizacao);
    }

    // SOLICITA A ASSOCIAÇÃO DO USUÁRIO COM UM GRUPO PESQUISA
    public void solicitarAssociacaoGrupoPesquisa(UUID idUsuario, UUID idGrupoPesquisa) {
        User usuario = userRepository.findById(idUsuario)
                .orElseThrow(() -> new DadoNaoEncontradoException("Usuário não encontrada com o ID: " + idUsuario));

        GrupoPesquisa grupoPesquisa = grupoPesquisaRepository.findById(idGrupoPesquisa)
                .orElseThrow(() -> new DadoNaoEncontradoException("Grupo Pesquisa não encontrada com o ID: " + idGrupoPesquisa));

        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setGrupoPesquisaRequested(grupoPesquisa);
        solicitacao.setUserApplicant(usuario);
        solicitacao.setStatus(StatusSolicitacao.ATIVA);

        grupoPesquisa.setSolicitacoes(new ArrayList<>());
        grupoPesquisa.getSolicitacoes().add(solicitacao);
        grupoPesquisaRepository.save(grupoPesquisa);
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
