package com.marketplace.ifba.service;

import com.marketplace.ifba.exception.AdmException;
import com.marketplace.ifba.exception.InstituicaoInvalidaException;
import com.marketplace.ifba.exception.UsuarioInvalidoException;
import com.marketplace.ifba.model.Instituicao;
import com.marketplace.ifba.model.User;
import com.marketplace.ifba.model.enums.StatusInstituicao;
import com.marketplace.ifba.repository.InstituicaoRepository;
import com.marketplace.ifba.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InstituicaoServiceTest {

    @Mock
    private InstituicaoRepository instituicaoRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private InstituicaoService instituicaoService;

    private UUID instituicaoId;
    private UUID usuarioId;
    private Instituicao instituicao;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        instituicaoId = UUID.randomUUID();
        usuarioId = UUID.randomUUID();

        user = new User();
        user.setIdUsuario(usuarioId);

        instituicao = new Instituicao();
        instituicao.setIdInstituicao(instituicaoId);
        instituicao.setNome("Instituto Teste");
        instituicao.setStatus(StatusInstituicao.AGUARDANDO_APROVACAO);
        instituicao.setUsuarioRegistro(user);
    }

    // --------- LEITURA

    @Test
    void deveBuscarInstituicaoPorId_QuandoExistir() {
        when(instituicaoRepository.findById(instituicaoId)).thenReturn(Optional.of(instituicao));
        Instituicao result = instituicaoService.buscarInstituicaoPorId(instituicaoId);
        assertEquals("Instituto Teste", result.getNome());
    }

    @Test
    void deveLancarExcecao_QuandoInstituicaoNaoEncontradaPorId() {
        when(instituicaoRepository.findById(instituicaoId)).thenReturn(Optional.empty());
        assertThrows(InstituicaoInvalidaException.class,
                () -> instituicaoService.buscarInstituicaoPorId(instituicaoId));
    }

    @Test
    void deveBuscarInstituicaoPorNome_QuandoExistir() {
        when(instituicaoRepository.findAll()).thenReturn(List.of(instituicao));
        Instituicao result = instituicaoService.buscarInstituicaoPorNome("Instituto Teste");
        assertNotNull(result);
    }

    @Test
    void deveLancarExcecao_QuandoInstituicaoNaoEncontradaPorNome() {
        when(instituicaoRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(InstituicaoInvalidaException.class,
                () -> instituicaoService.buscarInstituicaoPorNome("Inexistente"));
    }

    @Test
    void deveBuscarTodasInstituicoes() {
        when(instituicaoRepository.findAll()).thenReturn(List.of(instituicao));
        List<Instituicao> result = instituicaoService.buscarTodasInstituicoes();
        assertEquals(1, result.size());
    }

    // --------- ESCRITA

    @Test
    void deveRegistrarInstituicaoComSucesso() {
        Instituicao novaInstituicao = new Instituicao();
        novaInstituicao.setNome("Nova Instituicao");

        when(userRepository.findById(usuarioId)).thenReturn(Optional.of(user));
        when(instituicaoRepository.save(any(Instituicao.class))).thenAnswer(inv -> inv.getArgument(0));

        Instituicao result = instituicaoService.registrarInstituicao(novaInstituicao, usuarioId);

        assertEquals(StatusInstituicao.AGUARDANDO_APROVACAO, result.getStatus());
        assertEquals(user, result.getUsuarioRegistro());
        assertNotNull(result.getDataRegistro());
        assertEquals("Nova Instituicao", result.getNome());
    }

    @Test
    void deveLancarExcecao_AoRegistrarComUsuarioInvalido() {
        Instituicao novaInstituicao = new Instituicao();
        when(userRepository.findById(usuarioId)).thenReturn(Optional.empty());

        assertThrows(UsuarioInvalidoException.class,
                () -> instituicaoService.registrarInstituicao(novaInstituicao, usuarioId));
    }

    @Test
    void deveAtualizarInstituicaoComSucesso() {
        Instituicao update = new Instituicao();
        update.setNome("Instituto Atualizado");
        update.setSigla("IA");

        when(instituicaoRepository.findById(instituicaoId)).thenReturn(Optional.of(instituicao));
        when(instituicaoRepository.save(any(Instituicao.class))).thenAnswer(inv -> inv.getArgument(0));

        Instituicao result = instituicaoService.atualizarInstituicao(update, instituicaoId);

        assertEquals("Instituto Atualizado", result.getNome());
        assertEquals("IA", result.getSigla());
    }

    @Test
    void deveLancarExcecao_AoAtualizarInstituicaoInexistente() {
        when(instituicaoRepository.findById(instituicaoId)).thenReturn(Optional.empty());
        assertThrows(InstituicaoInvalidaException.class,
                () -> instituicaoService.atualizarInstituicao(instituicao, instituicaoId));
    }

    // --------- APROVACAO/REPROVACAO

    @Test
    void deveAprovarInstituicaoComSucesso() {
        User adm = new User();
        adm.setIdUsuario(UUID.randomUUID());

        when(instituicaoRepository.findById(instituicaoId)).thenReturn(Optional.of(instituicao));
        when(userRepository.findById(adm.getIdUsuario())).thenReturn(Optional.of(adm));
        when(instituicaoRepository.save(any(Instituicao.class))).thenAnswer(inv -> inv.getArgument(0));

        Instituicao result = instituicaoService.aprovarOuReprovaInstituicao(instituicaoId, adm.getIdUsuario(), true);

        assertEquals(StatusInstituicao.APROVADA, result.getStatus());
        assertEquals(adm, result.getAdmAprovacao());
        assertNotNull(result.getDataAprovacao());
    }

    @Test
    void deveReprovarInstituicaoComSucesso() {
        User adm = new User();
        adm.setIdUsuario(UUID.randomUUID());

        when(instituicaoRepository.findById(instituicaoId)).thenReturn(Optional.of(instituicao));
        when(userRepository.findById(adm.getIdUsuario())).thenReturn(Optional.of(adm));
        when(instituicaoRepository.save(any(Instituicao.class))).thenAnswer(inv -> inv.getArgument(0));

        Instituicao result = instituicaoService.aprovarOuReprovaInstituicao(instituicaoId, adm.getIdUsuario(), false);

        assertEquals(StatusInstituicao.NAO_APROVADA, result.getStatus());
        assertEquals(adm, result.getAdmAprovacao());
        assertNotNull(result.getDataAprovacao());
    }

    @Test
    void deveLancarExcecao_AoAprovarInstituicaoInvalida() {
        User adm = new User();
        adm.setIdUsuario(UUID.randomUUID());
        instituicao.setStatus(StatusInstituicao.APROVADA); // já aprovada

        when(instituicaoRepository.findById(instituicaoId)).thenReturn(Optional.of(instituicao));

        assertThrows(IllegalStateException.class,
                () -> instituicaoService.aprovarOuReprovaInstituicao(instituicaoId, adm.getIdUsuario(), true));
    }

    @Test
    void deveLancarExcecao_QuandoAdministradorInexistente() {
        when(instituicaoRepository.findById(instituicaoId)).thenReturn(Optional.of(instituicao));
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(AdmException.class,
                () -> instituicaoService.aprovarOuReprovaInstituicao(instituicaoId, UUID.randomUUID(), true));
    }

    // --------- REMOÇÃO

    @Test
    void deveRemoverInstituicaoComSucesso() {
        when(instituicaoRepository.existsById(instituicaoId)).thenReturn(true);
        instituicaoService.removerInstituicao(instituicaoId);
        verify(instituicaoRepository).deleteById(instituicaoId);
    }

    @Test
    void deveLancarExcecao_AoRemoverInstituicaoInexistente() {
        when(instituicaoRepository.existsById(instituicaoId)).thenReturn(false);
        assertThrows(InstituicaoInvalidaException.class, () -> instituicaoService.removerInstituicao(instituicaoId));
    }
}
