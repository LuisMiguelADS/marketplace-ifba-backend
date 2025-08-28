package com.marketplace.ifba.service;

import com.marketplace.ifba.exception.*;
import com.marketplace.ifba.model.*;
import com.marketplace.ifba.model.enums.StatusGrupoPesquisa;
import com.marketplace.ifba.model.enums.StatusSolicitacao;
import com.marketplace.ifba.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GrupoPesquisaServiceTest {

    @Mock
    private GrupoPesquisaRepository grupoPesquisaRepository;
    @Mock
    private InstituicaoRepository instituicaoRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AreaRepository areaRepository;
    @Mock
    private DemandaRepository demandaRepository;

    @InjectMocks
    private GrupoPesquisaService grupoPesquisaService;

    private UUID grupoId;
    private UUID instituicaoId;
    private UUID usuarioId;
    private GrupoPesquisa grupoPesquisa;
    private User user;
    private Instituicao instituicao;
    private Demanda demanda;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        grupoId = UUID.randomUUID();
        instituicaoId = UUID.randomUUID();
        usuarioId = UUID.randomUUID();

        instituicao = new Instituicao();
        instituicao.setIdInstituicao(instituicaoId);

        user = new User();
        user.setIdUsuario(usuarioId);

        grupoPesquisa = new GrupoPesquisa();
        grupoPesquisa.setIdGrupoPesquisa(grupoId);
        grupoPesquisa.setNome("Grupo Teste");
        grupoPesquisa.setInstituicao(instituicao);
        grupoPesquisa.setUsuarioRegistrador(user);

        demanda = new Demanda();
        demanda.setIdDemanda(UUID.randomUUID());
        demanda.setGruposPesquisa(new ArrayList<>());
    }

    // --------- LEITURA

    @Test
    void deveBuscarGrupoPesquisaPorId_QuandoExistir() {
        when(grupoPesquisaRepository.findById(grupoId)).thenReturn(Optional.of(grupoPesquisa));
        GrupoPesquisa result = grupoPesquisaService.buscarGrupoPesquisaPorId(grupoId);
        assertEquals("Grupo Teste", result.getNome());
    }

    @Test
    void deveLancarExcecao_QuandoGrupoPesquisaNaoEncontradoPorId() {
        when(grupoPesquisaRepository.findById(grupoId)).thenReturn(Optional.empty());
        assertThrows(GrupoPesquisaInvalidoException.class,
                () -> grupoPesquisaService.buscarGrupoPesquisaPorId(grupoId));
    }

    @Test
    void deveBuscarGrupoPesquisaPorNome_QuandoExistir() {
        when(grupoPesquisaRepository.findAll()).thenReturn(List.of(grupoPesquisa));
        GrupoPesquisa result = grupoPesquisaService.buscarGrupoPesquisaPorNome("Grupo Teste");
        assertNotNull(result);
    }

    @Test
    void deveLancarExcecao_QuandoGrupoPesquisaNaoEncontradoPorNome() {
        when(grupoPesquisaRepository.findAll()).thenReturn(List.of());
        assertThrows(GrupoPesquisaInvalidoException.class,
                () -> grupoPesquisaService.buscarGrupoPesquisaPorNome("Inexistente"));
    }

    @Test
    void deveBuscarGruposPorInstituicao_QuandoInstituicaoExiste() {
        when(instituicaoRepository.existsById(instituicaoId)).thenReturn(true);
        when(grupoPesquisaRepository.findAll()).thenReturn(List.of(grupoPesquisa));
        List<GrupoPesquisa> result = grupoPesquisaService.buscarGruposPorInstituicao(instituicaoId);
        assertEquals(1, result.size());
    }

    @Test
    void deveLancarExcecao_QuandoInstituicaoNaoEncontrada() {
        when(instituicaoRepository.existsById(instituicaoId)).thenReturn(false);
        assertThrows(InstituicaoInvalidaException.class,
                () -> grupoPesquisaService.buscarGruposPorInstituicao(instituicaoId));
    }

    @Test
    void deveBuscarUsuariosSolicitantesAssociacao() {
        Solicitacao sol = new Solicitacao();
        sol.setStatus(StatusSolicitacao.ATIVA);
        sol.setUserApplicant(user);
        grupoPesquisa.setSolicitacoes(List.of(sol));
        when(grupoPesquisaRepository.findById(grupoId)).thenReturn(Optional.of(grupoPesquisa));

        List<User> result = grupoPesquisaService.buscarUsuariosSolicitantesAssociacao(grupoId);
        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverSolicitacoes() {
        grupoPesquisa.setSolicitacoes(null);
        when(grupoPesquisaRepository.findById(grupoId)).thenReturn(Optional.of(grupoPesquisa));

        List<User> result = grupoPesquisaService.buscarUsuariosSolicitantesAssociacao(grupoId);
        assertTrue(result.isEmpty());
    }

    @Test
    void deveBuscarDemandasRecebidas() {
        grupoPesquisa.setDemandas(List.of(demanda));
        when(grupoPesquisaRepository.findById(grupoId)).thenReturn(Optional.of(grupoPesquisa));

        List<Demanda> result = grupoPesquisaService.buscarDemandasRecebidas(grupoId);
        assertEquals(1, result.size());
        assertEquals(demanda, result.get(0));
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverDemandas() {
        grupoPesquisa.setDemandas(null);
        when(grupoPesquisaRepository.findById(grupoId)).thenReturn(Optional.of(grupoPesquisa));

        List<Demanda> result = grupoPesquisaService.buscarDemandasRecebidas(grupoId);
        assertTrue(result.isEmpty());
    }

    @Test
    void deveBuscarTodosGruposPesquisa() {
        when(grupoPesquisaRepository.findAll()).thenReturn(List.of(grupoPesquisa));
        List<GrupoPesquisa> result = grupoPesquisaService.buscarTodosGruposPesquisa();
        assertEquals(1, result.size());
    }

    // --------- ESCRITA

    @Test
    void deveRegistrarGrupoPesquisaComSucesso() {
        GrupoPesquisa novoGrupo = new GrupoPesquisa();
        novoGrupo.setNome("Novo Grupo");

        when(grupoPesquisaRepository.findAll()).thenReturn(List.of());
        when(instituicaoRepository.findById(instituicaoId)).thenReturn(Optional.of(instituicao));
        when(userRepository.findById(usuarioId)).thenReturn(Optional.of(user));
        when(grupoPesquisaRepository.save(any(GrupoPesquisa.class))).thenAnswer(inv -> inv.getArgument(0));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        GrupoPesquisa result = grupoPesquisaService.registrarGrupoPesquisa(
                novoGrupo, instituicaoId, usuarioId, List.of());

        assertEquals("Novo Grupo", result.getNome());
        assertEquals(StatusGrupoPesquisa.ATIVO, result.getStatus());
        verify(grupoPesquisaRepository).save(novoGrupo);
        verify(userRepository).save(user);
    }

    @Test
    void deveLancarExcecao_AoRegistrarComNomeDuplicado() {
        GrupoPesquisa duplicado = new GrupoPesquisa();
        duplicado.setNome("Grupo Teste");
        when(grupoPesquisaRepository.findAll()).thenReturn(List.of(duplicado));
        assertThrows(GrupoPesquisaInvalidoException.class,
                () -> grupoPesquisaService.registrarGrupoPesquisa(grupoPesquisa, instituicaoId, usuarioId, List.of()));
    }

    @Test
    void deveAtualizarGrupoPesquisa_QuandoExistir() {
        GrupoPesquisa update = new GrupoPesquisa();
        update.setNome("Novo Nome");
        update.setAreas(new ArrayList<>());
        update.setDescricao("Nova Desc");

        when(grupoPesquisaRepository.findById(grupoId)).thenReturn(Optional.of(grupoPesquisa));
        when(grupoPesquisaRepository.findAll()).thenReturn(List.of(grupoPesquisa));
        when(grupoPesquisaRepository.save(any(GrupoPesquisa.class))).thenAnswer(inv -> inv.getArgument(0));

        GrupoPesquisa result = grupoPesquisaService.atualizarGrupoPesquisa(grupoId, update);
        assertEquals("Novo Nome", result.getNome());
        assertEquals("Nova Desc", result.getDescricao());
    }

    @Test
    void deveLancarExcecao_AoAtualizarGrupoInexistente() {
        when(grupoPesquisaRepository.findById(grupoId)).thenReturn(Optional.empty());
        assertThrows(GrupoPesquisaInvalidoException.class,
                () -> grupoPesquisaService.atualizarGrupoPesquisa(grupoId, grupoPesquisa));
    }

    @Test
    void deveAtualizarStatusGrupoPesquisa() {
        when(grupoPesquisaRepository.findById(grupoId)).thenReturn(Optional.of(grupoPesquisa));
        when(grupoPesquisaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        GrupoPesquisa result = grupoPesquisaService.atualizarStatusGrupoPesquisa(grupoId, StatusGrupoPesquisa.INATIVO);
        assertEquals(StatusGrupoPesquisa.INATIVO, result.getStatus());
    }

    @Test
    void deveRemoverGrupoPesquisaComSucesso() {
        when(grupoPesquisaRepository.existsById(grupoId)).thenReturn(true);
        grupoPesquisaService.removerGrupoPesquisa(grupoId);
        verify(grupoPesquisaRepository).deleteById(grupoId);
    }

    @Test
    void deveLancarExcecao_AoRemoverGrupoInexistente() {
        when(grupoPesquisaRepository.existsById(grupoId)).thenReturn(false);
        assertThrows(GrupoPesquisaInvalidoException.class,
                () -> grupoPesquisaService.removerGrupoPesquisa(grupoId));
    }

    @Test
    void deveRemoverDemandaDoGrupoComSucesso() {
        grupoPesquisa.setDemandas(new ArrayList<>(List.of(demanda)));
        demanda.getGruposPesquisa().add(grupoPesquisa);

        when(grupoPesquisaRepository.findById(grupoId)).thenReturn(Optional.of(grupoPesquisa));
        when(demandaRepository.findById(demanda.getIdDemanda())).thenReturn(Optional.of(demanda));
        when(grupoPesquisaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(demandaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        grupoPesquisaService.removerDemandaDoGrupo(grupoId, demanda.getIdDemanda());

        assertFalse(grupoPesquisa.getDemandas().contains(demanda));
        assertFalse(demanda.getGruposPesquisa().contains(grupoPesquisa));
    }

    @Test
    void deveLancarExcecao_AoRemoverDemandaNaoAssociada() {
        grupoPesquisa.setDemandas(new ArrayList<>());
        when(grupoPesquisaRepository.findById(grupoId)).thenReturn(Optional.of(grupoPesquisa));
        when(demandaRepository.findById(demanda.getIdDemanda())).thenReturn(Optional.of(demanda));

        assertThrows(DemandaInvalidaException.class,
                () -> grupoPesquisaService.removerDemandaDoGrupo(grupoId, demanda.getIdDemanda()));
    }

}
