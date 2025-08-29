package com.marketplace.ifba.service;

import com.marketplace.ifba.exception.DemandaInvalidaException;
import com.marketplace.ifba.exception.GrupoPesquisaInvalidoException;
import com.marketplace.ifba.exception.OfertaSolucaoInvalidaException;
import com.marketplace.ifba.model.Demanda;
import com.marketplace.ifba.model.GrupoPesquisa;
import com.marketplace.ifba.model.OfertaSolucao;
import com.marketplace.ifba.model.enums.StatusOfertaSolucao;
import com.marketplace.ifba.repository.DemandaRepository;
import com.marketplace.ifba.repository.GrupoPesquisaRepository;
import com.marketplace.ifba.repository.OfertaSolucaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OfertaSolucaoServiceTest {

    @Mock
    private OfertaSolucaoRepository ofertaSolucaoRepository;

    @Mock
    private DemandaRepository demandaRepository;

    @Mock
    private GrupoPesquisaRepository grupoPesquisaRepository;

    @InjectMocks
    private OfertaSolucaoService ofertaSolucaoService;

    private OfertaSolucao ofertaSolucao;
    private Demanda demanda;
    private GrupoPesquisa grupoPesquisa;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        grupoPesquisa = new GrupoPesquisa();
        grupoPesquisa.setIdGrupoPesquisa(UUID.randomUUID());

        demanda = new Demanda();
        demanda.setIdDemanda(UUID.randomUUID());
        demanda.setOfertasSolucoes(new ArrayList<>());

        ofertaSolucao = new OfertaSolucao();
        ofertaSolucao.setIdSolucao(UUID.randomUUID());
        ofertaSolucao.setNome("Solução Teste");
        ofertaSolucao.setStatus(StatusOfertaSolucao.AGUARDANDO_APROVACAO);
        ofertaSolucao.setGrupoPesquisa(grupoPesquisa);
        ofertaSolucao.setDemanda(demanda);
        ofertaSolucao.setDataRegistro(LocalDateTime.now());
    }

    @Test
    void deveBuscarOfertaSolucaoPorId_ComSucesso() {
        when(ofertaSolucaoRepository.findById(ofertaSolucao.getIdSolucao()))
                .thenReturn(Optional.of(ofertaSolucao));

        OfertaSolucao resultado = ofertaSolucaoService.buscarOfertaSolucaoPorId(ofertaSolucao.getIdSolucao());

        assertNotNull(resultado);
        assertEquals("Solução Teste", resultado.getNome());
        verify(ofertaSolucaoRepository, times(1)).findById(ofertaSolucao.getIdSolucao());
    }

    @Test
    void deveLancarExcecao_QuandoNaoEncontrarPorId() {
        when(ofertaSolucaoRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(OfertaSolucaoInvalidaException.class,
                () -> ofertaSolucaoService.buscarOfertaSolucaoPorId(UUID.randomUUID()));
    }

    @Test
    void deveBuscarOfertaPorNome_ComSucesso() {
        when(ofertaSolucaoRepository.findAll()).thenReturn(List.of(ofertaSolucao));

        OfertaSolucao resultado = ofertaSolucaoService.buscarOfertasSolucaoPorNome("Solução Teste");

        assertNotNull(resultado);
        assertEquals("Solução Teste", resultado.getNome());
    }

    @Test
    void deveBuscarPorStatus_ComSucesso() {
        when(ofertaSolucaoRepository.findAll()).thenReturn(List.of(ofertaSolucao));

        List<OfertaSolucao> resultados = ofertaSolucaoService
                .buscarOfertasSolucaoPorStatus(StatusOfertaSolucao.AGUARDANDO_APROVACAO);

        assertEquals(1, resultados.size());
        assertEquals(StatusOfertaSolucao.AGUARDANDO_APROVACAO, resultados.get(0).getStatus());
    }

    @Test
    void deveBuscarPorGrupoPesquisa_ComSucesso() {
        when(grupoPesquisaRepository.existsById(grupoPesquisa.getIdGrupoPesquisa())).thenReturn(true);
        when(ofertaSolucaoRepository.findAll()).thenReturn(List.of(ofertaSolucao));

        List<OfertaSolucao> resultados = ofertaSolucaoService
                .buscarOfertasSolucaoPorGrupoPesquisa(grupoPesquisa.getIdGrupoPesquisa());

        assertEquals(1, resultados.size());
        assertEquals(grupoPesquisa.getIdGrupoPesquisa(), resultados.get(0).getGrupoPesquisa().getIdGrupoPesquisa());
    }

    @Test
    void deveLancarExcecao_QuandoGrupoPesquisaNaoExistir() {
        when(grupoPesquisaRepository.existsById(any())).thenReturn(false);

        assertThrows(GrupoPesquisaInvalidoException.class,
                () -> ofertaSolucaoService.buscarOfertasSolucaoPorGrupoPesquisa(UUID.randomUUID()));
    }

//    @Test
//    void deveRegistrarOfertaSolucao_ComSucesso() {
//        when(demandaRepository.findById(demanda.getIdDemanda())).thenReturn(Optional.of(demanda));
//        when(ofertaSolucaoRepository.save(any(OfertaSolucao.class))).thenReturn(ofertaSolucao);
//
//        OfertaSolucao resultado = ofertaSolucaoService.registrarOfertaSolucao(demanda.getIdDemanda(), ofertaSolucao);
//
//        assertNotNull(resultado);
//        assertEquals(StatusOfertaSolucao.AGUARDANDO_APROVACAO, resultado.getStatus());
//        verify(ofertaSolucaoRepository, times(1)).save(ofertaSolucao);
//    }
//
//    @Test
//    void deveLancarExcecao_QuandoDemandaNaoExistir() {
//        when(demandaRepository.findById(any())).thenReturn(Optional.empty());
//
//        assertThrows(DemandaInvalidaException.class,
//                () -> ofertaSolucaoService.registrarOfertaSolucao(UUID.randomUUID(), ofertaSolucao));
//    }

    @Test
    void deveAtualizarOfertaSolucao_ComSucesso() {
        OfertaSolucao nova = new OfertaSolucao();
        nova.setNome("Novo Nome");
        nova.setDescricao("Nova descrição");

        when(ofertaSolucaoRepository.findById(ofertaSolucao.getIdSolucao())).thenReturn(Optional.of(ofertaSolucao));
        when(ofertaSolucaoRepository.save(any(OfertaSolucao.class))).thenReturn(ofertaSolucao);

        OfertaSolucao resultado = ofertaSolucaoService.atualizarOfertaSolucao(ofertaSolucao.getIdSolucao(), nova);

        assertEquals("Novo Nome", resultado.getNome());
        assertEquals("Nova descrição", resultado.getDescricao());
    }

    @Test
    void deveAprovarOfertaSolucao() {
        when(ofertaSolucaoRepository.findById(ofertaSolucao.getIdSolucao())).thenReturn(Optional.of(ofertaSolucao));
        when(ofertaSolucaoRepository.save(any(OfertaSolucao.class))).thenReturn(ofertaSolucao);

        OfertaSolucao resultado = ofertaSolucaoService.aprovarOuReprovarOfertaSolucao(ofertaSolucao.getIdSolucao(),
                true);

        assertEquals(StatusOfertaSolucao.APROVADA, resultado.getStatus());
    }

    @Test
    void deveReprovarOfertaSolucao() {
        when(ofertaSolucaoRepository.findById(ofertaSolucao.getIdSolucao())).thenReturn(Optional.of(ofertaSolucao));
        when(ofertaSolucaoRepository.save(any(OfertaSolucao.class))).thenReturn(ofertaSolucao);

        OfertaSolucao resultado = ofertaSolucaoService.aprovarOuReprovarOfertaSolucao(ofertaSolucao.getIdSolucao(),
                false);

        assertEquals(StatusOfertaSolucao.REPROVADA, resultado.getStatus());
    }

    @Test
    void deveRemoverOfertaSolucao_ComSucesso() {
        when(ofertaSolucaoRepository.existsById(ofertaSolucao.getIdSolucao())).thenReturn(true);
        doNothing().when(ofertaSolucaoRepository).deleteById(ofertaSolucao.getIdSolucao());

        ofertaSolucaoService.removerOfertaSolucao(ofertaSolucao.getIdSolucao());

        verify(ofertaSolucaoRepository, times(1)).deleteById(ofertaSolucao.getIdSolucao());
    }

    @Test
    void deveLancarExcecao_QuandoRemoverOfertaInexistente() {
        when(ofertaSolucaoRepository.existsById(any())).thenReturn(false);

        assertThrows(OfertaSolucaoInvalidaException.class,
                () -> ofertaSolucaoService.removerOfertaSolucao(UUID.randomUUID()));
    }
}
