package com.marketplace.ifba.service;

import com.marketplace.ifba.exception.*;
import com.marketplace.ifba.model.Demanda;
import com.marketplace.ifba.model.GrupoPesquisa;
import com.marketplace.ifba.model.Organizacao;
import com.marketplace.ifba.model.User;
import com.marketplace.ifba.model.enums.StatusDemanda;
import com.marketplace.ifba.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DemandaServiceTest {

    private DemandaRepository demandaRepository;
    private UserRepository userRepository;
    private OrganizacaoRepository organizacaoRepository;
    private GrupoPesquisaRepository grupoPesquisaRepository;
    private DemandaService demandaService;

    @BeforeEach
    void setUp() {
        demandaRepository = mock(DemandaRepository.class);
        userRepository = mock(UserRepository.class);
        organizacaoRepository = mock(OrganizacaoRepository.class);
        grupoPesquisaRepository = mock(GrupoPesquisaRepository.class);
        demandaService = new DemandaService(demandaRepository, userRepository, organizacaoRepository,
                grupoPesquisaRepository);
    }

    @Test
    void deveRegistrarDemandaComSucesso() {
        UUID userId = UUID.randomUUID();
        UUID orgId = UUID.randomUUID();

        User user = new User();
        user.setIdUsuario(orgId);

        Organizacao org = new Organizacao();
        org.setIdOrganizacao(orgId);

        Demanda demanda = new Demanda();
        demanda.setNome("Teste");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(organizacaoRepository.findById(orgId)).thenReturn(Optional.of(org));
        when(demandaRepository.save(any(Demanda.class))).thenAnswer(inv -> inv.getArgument(0));

        Demanda result = demandaService.registrarDemanda(demanda, userId, orgId);

        ArgumentCaptor<Demanda> captor = ArgumentCaptor.forClass(Demanda.class);
        verify(demandaRepository).save(captor.capture());
        Demanda saved = captor.getValue();

        assertEquals(StatusDemanda.AGUARDANDO_APROVACAO, saved.getStatus());
        assertEquals(0, saved.getVisualizacoes());
        assertEquals(user, saved.getUsuarioRegistrador());
        assertEquals(org, saved.getOrganizacao());
    }

    @Test
    void deveAtualizarDemandaComSucesso() {
        UUID id = UUID.randomUUID();
        Demanda original = new Demanda();
        original.setIdDemanda(id);

        Demanda nova = new Demanda();
        nova.setNome("Novo");
        nova.setDescricao("Descrição");
        nova.setCriterio("Critério");
        nova.setOrcamento(1000.0);
        nova.setDataPrazoFinal(LocalDate.now().plusDays(5));

        when(demandaRepository.findById(id)).thenReturn(Optional.of(original));
        when(demandaRepository.save(any(Demanda.class))).thenAnswer(inv -> inv.getArgument(0));

        Demanda result = demandaService.atualizarDemanda(nova, id);

        ArgumentCaptor<Demanda> captor = ArgumentCaptor.forClass(Demanda.class);
        verify(demandaRepository).save(captor.capture());
        Demanda saved = captor.getValue();

        assertEquals("Novo", saved.getNome());
        assertEquals("Descrição", saved.getDescricao());
        assertEquals("Critério", saved.getCriterio());
        assertEquals(1000.0, saved.getOrcamento());
    }

    @Test
    void deveAtualizarStatusDemanda() {
        UUID id = UUID.randomUUID();
        Demanda demanda = new Demanda();
        demanda.setIdDemanda(id);

        when(demandaRepository.findById(id)).thenReturn(Optional.of(demanda));
        when(demandaRepository.save(any(Demanda.class))).thenAnswer(inv -> inv.getArgument(0));

        Demanda result = demandaService.atualizarStatusDemanda(id, StatusDemanda.FINALIZADA);

        ArgumentCaptor<Demanda> captor = ArgumentCaptor.forClass(Demanda.class);
        verify(demandaRepository).save(captor.capture());
        Demanda saved = captor.getValue();

        assertEquals(StatusDemanda.FINALIZADA, saved.getStatus());

    }

    @Test
    void deveAprovarDemandaQuandoDecisaoPositiva() {
        UUID id = UUID.randomUUID();
        Demanda demanda = new Demanda();
        demanda.setIdDemanda(id);
        demanda.setStatus(StatusDemanda.AGUARDANDO_APROVACAO);

        when(demandaRepository.findById(id)).thenReturn(Optional.of(demanda));
        when(demandaRepository.save(any(Demanda.class))).thenAnswer(inv -> inv.getArgument(0));

        Demanda result = demandaService.aprovarDemandaDemandante(id, true);

        ArgumentCaptor<Demanda> captor = ArgumentCaptor.forClass(Demanda.class);
        verify(demandaRepository).save(captor.capture());
        Demanda saved = captor.getValue();

        assertEquals(StatusDemanda.AGUARDANDO_PROPOSTA, saved.getStatus());
        assertNotNull(saved.getDataAprovado());
        assertEquals(LocalDate.now(), saved.getDataAprovado());
    }

    @Test
    void deveReprovarDemandaQuandoDecisaoNegativa() {
        UUID id = UUID.randomUUID();
        Demanda demanda = new Demanda();
        demanda.setIdDemanda(id);
        demanda.setStatus(StatusDemanda.AGUARDANDO_APROVACAO);

        when(demandaRepository.findById(id)).thenReturn(Optional.of(demanda));
        when(demandaRepository.save(any(Demanda.class))).thenAnswer(inv -> inv.getArgument(0));

        Demanda result = demandaService.aprovarDemandaDemandante(id, false);

        ArgumentCaptor<Demanda> captor = ArgumentCaptor.forClass(Demanda.class);
        verify(demandaRepository).save(captor.capture());
        Demanda saved = captor.getValue();

        assertEquals(StatusDemanda.NAO_APROVADA, saved.getStatus());
        assertNotNull(saved.getDataAprovado());
        assertEquals(LocalDate.now(), saved.getDataAprovado());
    }

    @Test
    void deveEnviarDemandaParaGrupoComSucesso() {
        UUID demandaId = UUID.randomUUID();
        UUID grupoId = UUID.randomUUID();

        Demanda demanda = new Demanda();
        demanda.setIdDemanda(demandaId);

        GrupoPesquisa grupo = new GrupoPesquisa();
        grupo.setIdGrupoPesquisa(grupoId);
        grupo.setDemandas(new ArrayList<>());

        when(demandaRepository.findById(demandaId)).thenReturn(Optional.of(demanda));
        when(grupoPesquisaRepository.findById(grupoId)).thenReturn(Optional.of(grupo));

        demandaService.enviarDemandaParaGrupo(demandaId, grupoId);

        ArgumentCaptor<GrupoPesquisa> captor = ArgumentCaptor.forClass(GrupoPesquisa.class);
        verify(grupoPesquisaRepository).save(captor.capture());
        GrupoPesquisa saved = captor.getValue();

        assertTrue(saved.getDemandas().contains(demanda));
    }

    @Test
    void deveBuscarDemandaPorIdQuandoExistir() {
        UUID id = UUID.randomUUID();
        Demanda demanda = new Demanda();
        demanda.setIdDemanda(id);

        when(demandaRepository.findById(id)).thenReturn(Optional.of(demanda));

        Demanda result = demandaService.buscarDemandaPorId(id);

        // Verifica se findById foi chamado com o ID correto
        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);
        verify(demandaRepository).findById(captor.capture());
        assertEquals(id, captor.getValue());

        assertNotNull(result);
        assertEquals(id, result.getIdDemanda());
    }

    @Test
    void deveLancarExcecaoAoBuscarDemandaPorIdInexistente() {
        UUID id = UUID.randomUUID();
        when(demandaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DemandaInvalidaException.class, () -> demandaService.buscarDemandaPorId(id));

        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);
        verify(demandaRepository).findById(captor.capture());
        assertEquals(id, captor.getValue());
    }

    @Test
    void deveBuscarDemandaPorNomeQuandoExistir() {
        Demanda demanda = new Demanda();
        demanda.setNome("Projeto A");

        when(demandaRepository.findAll()).thenReturn(List.of(demanda));

        Demanda result = demandaService.buscarDemandaPorNome("Projeto A");

        // Verifica se findAll foi chamado
        verify(demandaRepository).findAll();

        assertNotNull(result);
        assertEquals("Projeto A", result.getNome());
    }

    @Test
    void deveLancarExcecaoQuandoDemandaPorNomeNaoExistir() {
        when(demandaRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(DemandaInvalidaException.class, () -> demandaService.buscarDemandaPorNome("NaoExiste"));

        verify(demandaRepository).findAll();
    }

    @Test
    void deveIncrementarVisualizacao() {
        UUID id = UUID.randomUUID();
        Demanda demanda = new Demanda();
        demanda.setIdDemanda(id);
        demanda.setVisualizacoes(3);

        when(demandaRepository.findById(id)).thenReturn(Optional.of(demanda));
        when(demandaRepository.save(any(Demanda.class))).thenAnswer(inv -> inv.getArgument(0));

        demandaService.incrementarVizualizacao(id);

        // Captura o objeto salvo
        ArgumentCaptor<Demanda> captor = ArgumentCaptor.forClass(Demanda.class);
        verify(demandaRepository).save(captor.capture());
        Demanda saved = captor.getValue();

        assertEquals(4, saved.getVisualizacoes());
    }

    @Test
    void deveBuscarDemandasPorOrganizacaoQuandoOrganizacaoExistir() {
        UUID orgId = UUID.randomUUID();
        Organizacao org = new Organizacao();
        org.setIdOrganizacao(orgId);

        Demanda demanda = new Demanda();
        demanda.setOrganizacao(org);

        when(organizacaoRepository.existsById(orgId)).thenReturn(true);
        when(demandaRepository.findAll()).thenReturn(List.of(demanda));

        List<Demanda> result = demandaService.buscarDemandasPorOrganizacao(orgId);

        verify(organizacaoRepository).existsById(orgId);
        verify(demandaRepository).findAll();

        assertEquals(1, result.size());
        assertEquals(orgId, result.get(0).getOrganizacao().getIdOrganizacao());
    }

    @Test
    void deveBuscarDemandasAprovadas() {
        Demanda d1 = new Demanda();
        d1.setStatus(StatusDemanda.AGUARDANDO_PROPOSTA);

        Demanda d2 = new Demanda();
        d2.setStatus(StatusDemanda.FINALIZADA);

        when(demandaRepository.findAll()).thenReturn(List.of(d1, d2));

        List<Demanda> result = demandaService.buscarDemandasAprovadas();

        verify(demandaRepository).findAll();

        assertEquals(1, result.size());
        assertEquals(StatusDemanda.AGUARDANDO_PROPOSTA, result.get(0).getStatus());
    }

    @Test
    void deveBuscarTodasDemandas() {
        when(demandaRepository.findAll()).thenReturn(List.of(new Demanda(), new Demanda()));

        List<Demanda> result = demandaService.buscarTodasDemandas();

        verify(demandaRepository).findAll();

        assertEquals(2, result.size());
    }

}
