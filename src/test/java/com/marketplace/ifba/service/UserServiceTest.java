package com.marketplace.ifba.service;

import com.marketplace.ifba.exception.*;
import com.marketplace.ifba.model.*;
import com.marketplace.ifba.model.enums.StatusSolicitacao;
import com.marketplace.ifba.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TokenService tokenService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private OrganizacaoRepository organizacaoRepository;
    @Mock
    private InstituicaoRepository instituicaoRepository;
    @Mock
    private GrupoPesquisaRepository grupoPesquisaRepository;
    @Mock
    private SolicitacaoRepository solicitacaoRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private Instituicao instituicao;
    private Organizacao organizacao;
    private GrupoPesquisa grupoPesquisa;
    private Solicitacao solicitacao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setIdUsuario(UUID.randomUUID());
        user.setEmail("test@example.com");
        user.setCpf("12345678900");
        user.setPassword("password");
        user.setNomeCompleto("Test User");
        user.setTelefone("999999999");
        user.setBiografia("Bio");
        user.setConexoes(new ArrayList<>());

        instituicao = new Instituicao();
        instituicao.setIdInstituicao(UUID.randomUUID());
        instituicao.setUsuariosIntegrantes(new ArrayList<>());
        instituicao.setSolicitacoes(new ArrayList<>());

        organizacao = new Organizacao();
        organizacao.setIdOrganizacao(UUID.randomUUID());
        organizacao.setUsuariosIntegrantes(new ArrayList<>());
        organizacao.setSolicitacoes(new ArrayList<>());

        grupoPesquisa = new GrupoPesquisa();
        grupoPesquisa.setIdGrupoPesquisa(UUID.randomUUID());
        grupoPesquisa.setUsuarios(new ArrayList<>());
        grupoPesquisa.setSolicitacoes(new ArrayList<>());

        solicitacao = new Solicitacao();
        solicitacao.setUserApplicant(user);
        solicitacao.setStatus(StatusSolicitacao.ATIVA);
    }

    @Test
    void buscarUsuarioPorToken_DeveRetornarUsuario() {
        when(tokenService.validateToken(anyString())).thenReturn(user.getEmail());
        when(userRepository.findAll()).thenReturn(List.of(user));

        User result = userService.buscarUsuarioPorToken("token");
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void buscarUsuarioPorToken_DeveLancarExcecao() {
        when(tokenService.validateToken(anyString())).thenReturn("notfound@example.com");
        when(userRepository.findAll()).thenReturn(List.of(user));

        assertThrows(UsuarioInvalidoException.class, () -> userService.buscarUsuarioPorToken("token"));
    }

    @Test
    void buscarUsuarioPorID_DeveRetornarUsuario() {
        when(userRepository.findById(user.getIdUsuario())).thenReturn(Optional.of(user));
        User result = userService.buscarUsuarioPorID(user.getIdUsuario());
        assertEquals(user.getIdUsuario(), result.getIdUsuario());
    }

    @Test
    void buscarUsuarioPorID_DeveLancarExcecao() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(UsuarioInvalidoException.class, () -> userService.buscarUsuarioPorID(UUID.randomUUID()));
    }

    @Test
    void buscarUsuarioPorEmail_DeveRetornarUsuario() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        User result = userService.buscarUsuarioPorEmail(user.getEmail());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void buscarUsuarioPorEmail_DeveRetornarNull() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        User result = userService.buscarUsuarioPorEmail("notfound@example.com");
        assertNull(result);
    }

    @Test
    void buscarTodosUsuarios_DeveRetornarLista() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        List<User> result = userService.buscarTodosUsuarios();
        assertEquals(1, result.size());
    }

    @Test
    void registrarUsuario_DeveRegistrarComSucesso() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.registrarUsuario(user);
        assertEquals("encoded", result.getPassword());
        assertNotNull(result.getDataRegistro());
    }

    @Test
    void registrarUsuario_DeveLancarExcecaoEmail() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        User novo = new User();
        novo.setEmail(user.getEmail());
        novo.setCpf("00000000000");
        assertThrows(EmailInvalidoException.class, () -> userService.registrarUsuario(novo));
    }

    @Test
    void registrarUsuario_DeveLancarExcecaoCpf() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        User novo = new User();
        novo.setEmail("novo@example.com");
        novo.setCpf(user.getCpf());
        assertThrows(UsuarioInvalidoException.class, () -> userService.registrarUsuario(novo));
    }

    @Test
    void associarInstituicaoUsuario_DeveAssociarComSucesso() {
        when(userRepository.findById(user.getIdUsuario())).thenReturn(Optional.of(user));
        when(instituicaoRepository.findById(instituicao.getIdInstituicao())).thenReturn(Optional.of(instituicao));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(instituicaoRepository.save(any(Instituicao.class))).thenReturn(instituicao);

        userService.associarInstituicaoUsuario(user.getIdUsuario(), instituicao.getIdInstituicao(), true);
        assertTrue(instituicao.getUsuariosIntegrantes().contains(user));
    }

    @Test
    void associarInstituicaoUsuario_DeveRejeitarSolicitacao() {
        when(userRepository.findById(user.getIdUsuario())).thenReturn(Optional.of(user));
        when(instituicaoRepository.findById(instituicao.getIdInstituicao())).thenReturn(Optional.of(instituicao));
        instituicao.getSolicitacoes().add(solicitacao);

        userService.associarInstituicaoUsuario(user.getIdUsuario(), instituicao.getIdInstituicao(), false);
        assertEquals(StatusSolicitacao.REJEITADA, solicitacao.getStatus());
    }

    @Test
    void associarInstituicaoUsuario_DeveLancarExcecaoOrganizacao() {
        user.setInstituicao(null);
        user.setOrganizacao(new Organizacao());
        when(userRepository.findById(user.getIdUsuario())).thenReturn(Optional.of(user));
        when(instituicaoRepository.findById(instituicao.getIdInstituicao())).thenReturn(Optional.of(instituicao));
        assertThrows(InstituicaoInvalidaException.class, () -> userService
                .associarInstituicaoUsuario(user.getIdUsuario(), instituicao.getIdInstituicao(), true));
    }

    @Test
    void associarInstituicaoUsuario_DeveLancarExcecaoInstituicao() {
        user.setInstituicao(new Instituicao());
        user.setOrganizacao(null);
        when(userRepository.findById(user.getIdUsuario())).thenReturn(Optional.of(user));
        when(instituicaoRepository.findById(instituicao.getIdInstituicao())).thenReturn(Optional.of(instituicao));
        assertThrows(OrganizacaoInvalidaException.class, () -> userService
                .associarInstituicaoUsuario(user.getIdUsuario(), instituicao.getIdInstituicao(), true));
    }

    @Test
    void associarOrganizacaoUsuario_DeveAssociarComSucesso() {
        when(userRepository.findById(user.getIdUsuario())).thenReturn(Optional.of(user));
        when(organizacaoRepository.findById(organizacao.getIdOrganizacao())).thenReturn(Optional.of(organizacao));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(organizacaoRepository.save(any(Organizacao.class))).thenReturn(organizacao);

        userService.associarOrganizacaoUsuario(user.getIdUsuario(), organizacao.getIdOrganizacao(), true);
        assertTrue(organizacao.getUsuariosIntegrantes().contains(user));
    }

    @Test
    void associarOrganizacaoUsuario_DeveRejeitarSolicitacao() {
        when(userRepository.findById(user.getIdUsuario())).thenReturn(Optional.of(user));
        when(organizacaoRepository.findById(organizacao.getIdOrganizacao())).thenReturn(Optional.of(organizacao));
        organizacao.getSolicitacoes().add(solicitacao);

        userService.associarOrganizacaoUsuario(user.getIdUsuario(), organizacao.getIdOrganizacao(), false);
        assertEquals(StatusSolicitacao.REJEITADA, solicitacao.getStatus());
    }

    @Test
    void associarOrganizacaoUsuario_DeveLancarExcecaoOrganizacao() {
        user.setOrganizacao(new Organizacao());
        user.setInstituicao(null);
        when(userRepository.findById(user.getIdUsuario())).thenReturn(Optional.of(user));
        when(organizacaoRepository.findById(organizacao.getIdOrganizacao())).thenReturn(Optional.of(organizacao));
        assertThrows(OrganizacaoInvalidaException.class, () -> userService
                .associarOrganizacaoUsuario(user.getIdUsuario(), organizacao.getIdOrganizacao(), true));
    }

    @Test
    void associarOrganizacaoUsuario_DeveLancarExcecaoInstituicao() {
        user.setOrganizacao(null);
        user.setInstituicao(new Instituicao());
        when(userRepository.findById(user.getIdUsuario())).thenReturn(Optional.of(user));
        when(organizacaoRepository.findById(organizacao.getIdOrganizacao())).thenReturn(Optional.of(organizacao));
        assertThrows(InstituicaoInvalidaException.class, () -> userService
                .associarOrganizacaoUsuario(user.getIdUsuario(), organizacao.getIdOrganizacao(), true));
    }

    @Test
    void associarGrupoPesquisaUsuario_DeveAssociarComSucesso() {
        when(userRepository.findById(user.getIdUsuario())).thenReturn(Optional.of(user));
        when(grupoPesquisaRepository.findById(grupoPesquisa.getIdGrupoPesquisa()))
                .thenReturn(Optional.of(grupoPesquisa));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(grupoPesquisaRepository.save(any(GrupoPesquisa.class))).thenReturn(grupoPesquisa);

        userService.associarGrupoPesquisaUsuario(user.getIdUsuario(), grupoPesquisa.getIdGrupoPesquisa(), true);
        assertTrue(grupoPesquisa.getUsuarios().contains(user));
    }

    @Test
    void associarGrupoPesquisaUsuario_DeveRejeitarSolicitacao() {
        when(userRepository.findById(user.getIdUsuario())).thenReturn(Optional.of(user));
        when(grupoPesquisaRepository.findById(grupoPesquisa.getIdGrupoPesquisa()))
                .thenReturn(Optional.of(grupoPesquisa));
        grupoPesquisa.getSolicitacoes().add(solicitacao);

        userService.associarGrupoPesquisaUsuario(user.getIdUsuario(), grupoPesquisa.getIdGrupoPesquisa(), false);
        assertEquals(StatusSolicitacao.REJEITADA, solicitacao.getStatus());
    }

    @Test
    void associarGrupoPesquisaUsuario_DeveLancarExcecaoOrganizacao() {
        user.setOrganizacao(new Organizacao());
        user.setGrupoPesquisa(null);
        when(userRepository.findById(user.getIdUsuario())).thenReturn(Optional.of(user));
        when(grupoPesquisaRepository.findById(grupoPesquisa.getIdGrupoPesquisa()))
                .thenReturn(Optional.of(grupoPesquisa));
        assertThrows(OrganizacaoInvalidaException.class, () -> userService
                .associarGrupoPesquisaUsuario(user.getIdUsuario(), grupoPesquisa.getIdGrupoPesquisa(), true));
    }

    @Test
    void associarGrupoPesquisaUsuario_DeveLancarExcecaoGrupo() {
        user.setOrganizacao(null);
        user.setGrupoPesquisa(new GrupoPesquisa());
        when(userRepository.findById(user.getIdUsuario())).thenReturn(Optional.of(user));
        when(grupoPesquisaRepository.findById(grupoPesquisa.getIdGrupoPesquisa()))
                .thenReturn(Optional.of(grupoPesquisa));
        assertThrows(GrupoPesquisaInvalidoException.class, () -> userService
                .associarGrupoPesquisaUsuario(user.getIdUsuario(), grupoPesquisa.getIdGrupoPesquisa(), true));
    }

    @Test
    void solicitarAssociacaoOrganizacao_DeveSolicitarComSucesso() {
        when(userRepository.findById(user.getIdUsuario())).thenReturn(Optional.of(user));
        when(organizacaoRepository.findById(organizacao.getIdOrganizacao())).thenReturn(Optional.of(organizacao));
        organizacao.setSolicitacoes(new ArrayList<>());

        userService.solicitarAssociacaoOrganizacao(user.getIdUsuario(), organizacao.getIdOrganizacao());
        assertEquals(1, organizacao.getSolicitacoes().size());
    }

    @Test
    void solicitarAssociacaoOrganizacao_DeveLancarExcecaoSolicitacaoExistente() {
        when(userRepository.findById(user.getIdUsuario())).thenReturn(Optional.of(user));
        when(organizacaoRepository.findById(organizacao.getIdOrganizacao())).thenReturn(Optional.of(organizacao));
        Solicitacao s = new Solicitacao();
        s.setUserApplicant(user);
        s.setStatus(StatusSolicitacao.ATIVA);
        organizacao.setSolicitacoes(List.of(s));

        assertThrows(OrganizacaoInvalidaException.class,
                () -> userService.solicitarAssociacaoOrganizacao(user.getIdUsuario(), organizacao.getIdOrganizacao()));
    }

    @Test
    void solicitarAssociacaoOrganizacao_DeveLancarExcecaoUsuarioJaAssociado() {
        user.setOrganizacao(new Organizacao());
        when(userRepository.findById(user.getIdUsuario())).thenReturn(Optional.of(user));
        when(organizacaoRepository.findById(organizacao.getIdOrganizacao())).thenReturn(Optional.of(organizacao));
        assertThrows(OrganizacaoInvalidaException.class,
                () -> userService.solicitarAssociacaoOrganizacao(user.getIdUsuario(), organizacao.getIdOrganizacao()));
    }

    @Test
    void solicitarAssociacaoGrupoPesquisa_DeveSolicitarComSucesso() {
        when(userRepository.findById(user.getIdUsuario())).thenReturn(Optional.of(user));
        when(grupoPesquisaRepository.findById(grupoPesquisa.getIdGrupoPesquisa()))
                .thenReturn(Optional.of(grupoPesquisa));
        grupoPesquisa.setSolicitacoes(null);

        userService.solicitarAssociacaoGrupoPesquisa(user.getIdUsuario(), grupoPesquisa.getIdGrupoPesquisa());
        verify(solicitacaoRepository, times(1)).save(any(Solicitacao.class));
    }

    @Test
    void registraLogin_DeveRegistrarComSucesso() {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getEmail(),
                user.getPassword());
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(user);
        when(tokenService.generateToken(any(User.class))).thenReturn("token");
        when(userRepository.save(any(User.class))).thenReturn(user);

        String result = userService.registraLogin(user.getEmail(), user.getPassword());
        assertEquals("token", result);
    }

    @Test
    void atualizarUsuario_DeveAtualizarComSucesso() {
        User novo = new User();
        novo.setNomeCompleto("Novo Nome");
        novo.setTelefone("888888888");
        novo.setBiografia("Nova Bio");

        when(userRepository.findById(user.getIdUsuario())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.atualizarUsuario(user.getIdUsuario(), novo);
        assertEquals("Novo Nome", result.getNomeCompleto());
    }

    @Test
    void atualizarUsuario_DeveLancarExcecao() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(UsuarioInvalidoException.class, () -> userService.atualizarUsuario(UUID.randomUUID(), user));
    }

    @Test
    void removerUsuario_DeveRemoverComSucesso() {
        when(userRepository.existsById(user.getIdUsuario())).thenReturn(true);
        doNothing().when(userRepository).deleteById(user.getIdUsuario());

        userService.removerUsuario(user.getIdUsuario());
        verify(userRepository, times(1)).deleteById(user.getIdUsuario());
    }

    @Test
    void removerUsuario_DeveLancarExcecao() {
        when(userRepository.existsById(any())).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> userService.removerUsuario(UUID.randomUUID()));
    }
}
