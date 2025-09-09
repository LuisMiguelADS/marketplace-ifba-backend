package com.marketplace.ifba.service;

import com.marketplace.ifba.exception.*;
import com.marketplace.ifba.mapper.PropostaMapper;
import com.marketplace.ifba.model.GrupoPesquisa;
import com.marketplace.ifba.model.Instituicao;
import com.marketplace.ifba.model.Proposta;
import com.marketplace.ifba.model.enums.StatusInstituicao;
import com.marketplace.ifba.model.enums.StatusProposta;
import com.marketplace.ifba.repository.GrupoPesquisaRepository;
import com.marketplace.ifba.repository.InstituicaoRepository;
import com.marketplace.ifba.repository.PropostaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PropostaServiceTest {

    @Mock
    private PropostaRepository propostaRepository;

    @Mock
    private GrupoPesquisaRepository grupoPesquisaRepository;

    @Mock
    private InstituicaoRepository instituicaoRepository;

    @Mock
    private PropostaMapper propostaMapper;

    @InjectMocks
    private PropostaService propostaService;

    private Proposta proposta;
    private GrupoPesquisa grupoPesquisa;
    private Instituicao instituicao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        grupoPesquisa = new GrupoPesquisa();
        grupoPesquisa.setIdGrupoPesquisa(UUID.randomUUID());

        instituicao = new Instituicao();
        instituicao.setIdInstituicao(UUID.randomUUID());
        instituicao.setStatus(StatusInstituicao.APROVADA);

        proposta = new Proposta();
        proposta.setIdProposta(UUID.randomUUID());
        proposta.setNome("Proposta Teste");
        proposta.setDescricao("Descrição");
        proposta.setResumo("Resumo");
        proposta.setOrcamento(1000.0);
        proposta.setSolucao("Solução");
        proposta.setRestricoes("Restrição");
        proposta.setRecursosNecessarios("Recursos");
        proposta.setStatus(StatusProposta.AGUARDANDO_APROVACAO);
        proposta.setDataRegistro(LocalDateTime.now());
        proposta.setGrupoPesquisa(grupoPesquisa);
        proposta.setInstituicao(instituicao);
    }

    @Test
    void deveBuscarPropostaPorId_ComSucesso() {
        when(propostaRepository.findById(proposta.getIdProposta())).thenReturn(Optional.of(proposta));

        Proposta resultado = propostaService.buscarPropostaPorId(proposta.getIdProposta());

        assertNotNull(resultado);
        assertEquals("Proposta Teste", resultado.getNome());
    }

    @Test
    void deveLancarExcecao_QuandoNaoEncontrarPorId() {
        when(propostaRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(PropostaInvalidaException.class,
                () -> propostaService.buscarPropostaPorId(UUID.randomUUID()));
    }

    @Test
    void deveBuscarPropostaPorNome_ComSucesso() {
        when(propostaRepository.findByNome("Proposta Teste")).thenReturn(Optional.of(proposta));

        Proposta resultado = propostaService.buscarPropostaPorNome("Proposta Teste");

        assertNotNull(resultado);
        assertEquals("Proposta Teste", resultado.getNome());
    }

    @Test
    void deveLancarExcecao_QuandoNaoEncontrarPorNome() {
        when(propostaRepository.findByNome(any())).thenReturn(Optional.empty());

        assertThrows(PropostaInvalidaException.class,
                () -> propostaService.buscarPropostaPorNome("Inexistente"));
    }

    @Test
    void deveBuscarPropostasPorGrupoPesquisa_ComSucesso() {
        when(grupoPesquisaRepository.existsById(grupoPesquisa.getIdGrupoPesquisa())).thenReturn(true);
        when(propostaRepository.findAll()).thenReturn(List.of(proposta));

        List<Proposta> resultados = propostaService.buscarPropostasPorGrupoPesquisa(grupoPesquisa.getIdGrupoPesquisa());

        assertEquals(1, resultados.size());
        assertEquals(grupoPesquisa.getIdGrupoPesquisa(), resultados.get(0).getGrupoPesquisa().getIdGrupoPesquisa());
    }

    @Test
    void deveLancarExcecao_QuandoGrupoPesquisaNaoExistir() {
        when(grupoPesquisaRepository.existsById(any())).thenReturn(false);

        assertThrows(GrupoPesquisaInvalidoException.class,
                () -> propostaService.buscarPropostasPorGrupoPesquisa(UUID.randomUUID()));
    }

    @Test
    void deveBuscarPropostasPorInstituicao_ComSucesso() {
        when(instituicaoRepository.existsById(instituicao.getIdInstituicao())).thenReturn(true);
        when(propostaRepository.findAll()).thenReturn(List.of(proposta));

        List<Proposta> resultados = propostaService.buscarPropostasPorInstituicao(instituicao.getIdInstituicao());

        assertEquals(1, resultados.size());
        assertEquals(instituicao.getIdInstituicao(), resultados.get(0).getInstituicao().getIdInstituicao());
    }

    @Test
    void deveLancarExcecao_QuandoInstituicaoNaoExistir() {
        when(instituicaoRepository.existsById(any())).thenReturn(false);

        assertThrows(InstituicaoInvalidaException.class,
                () -> propostaService.buscarPropostasPorInstituicao(UUID.randomUUID()));
    }

    @Test
    void deveBuscarTodasPropostas() {
        when(propostaRepository.findAll()).thenReturn(List.of(proposta));

        List<Proposta> resultados = propostaService.buscarTodasPropostas();

        assertEquals(1, resultados.size());
        assertEquals("Proposta Teste", resultados.get(0).getNome());
    }

    @Test
    void deveRegistrarProposta_ComSucesso() {
        when(grupoPesquisaRepository.findById(grupoPesquisa.getIdGrupoPesquisa()))
                .thenReturn(Optional.of(grupoPesquisa));
        when(instituicaoRepository.findById(instituicao.getIdInstituicao())).thenReturn(Optional.of(instituicao));
        when(propostaRepository.save(any(Proposta.class))).thenReturn(proposta);

        Proposta resultado = propostaService.registrarProposta(proposta, instituicao.getIdInstituicao(),
                grupoPesquisa.getIdGrupoPesquisa());

        assertNotNull(resultado);
        assertEquals(StatusProposta.AGUARDANDO_APROVACAO, resultado.getStatus());
        assertEquals(instituicao, resultado.getInstituicao());
        assertEquals(grupoPesquisa, resultado.getGrupoPesquisa());
    }

    @Test
    void deveLancarExcecao_QuandoGrupoPesquisaNaoExistirAoRegistrar() {
        when(grupoPesquisaRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(GrupoPesquisaInvalidoException.class,
                () -> propostaService.registrarProposta(proposta, instituicao.getIdInstituicao(), UUID.randomUUID()));
    }

    @Test
    void deveLancarExcecao_QuandoInstituicaoNaoExistirAoRegistrar() {
        when(grupoPesquisaRepository.findById(grupoPesquisa.getIdGrupoPesquisa()))
                .thenReturn(Optional.of(grupoPesquisa));
        when(instituicaoRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(InstituicaoInvalidaException.class,
                () -> propostaService.registrarProposta(proposta, UUID.randomUUID(),
                        grupoPesquisa.getIdGrupoPesquisa()));
    }

    @Test
    void deveAtualizarProposta_ComSucesso() {
        Proposta nova = new Proposta();
        nova.setIdProposta(proposta.getIdProposta());
        nova.setNome("Proposta Teste"); // mesmo nome para passar na validação
        nova.setDescricao("Nova descrição");

        when(propostaRepository.findById(proposta.getIdProposta())).thenReturn(Optional.of(proposta));
        when(propostaRepository.save(any(Proposta.class))).thenReturn(proposta);

        Proposta resultado = propostaService.atualizarProposta(proposta.getIdProposta(), nova);

        assertEquals("Nova descrição", resultado.getDescricao());
    }

    @Test
    void deveLancarExcecao_QuandoAtualizarComNomeDiferente() {
        Proposta nova = new Proposta();
        nova.setIdProposta(proposta.getIdProposta());
        nova.setNome("Outro Nome");

        when(propostaRepository.findById(proposta.getIdProposta())).thenReturn(Optional.of(proposta));

        assertThrows(PropostaInvalidaException.class,
                () -> propostaService.atualizarProposta(proposta.getIdProposta(), nova));
    }

    @Test
    void deveAprovarProposta_ComSucesso() {
        when(propostaRepository.findById(proposta.getIdProposta())).thenReturn(Optional.of(proposta));
        when(propostaRepository.save(any(Proposta.class))).thenReturn(proposta);

        Proposta resultado = propostaService.aprovarOuReprovarProposta(proposta.getIdProposta(), true);

        assertEquals(StatusProposta.APROVADA, resultado.getStatus());
    }

    @Test
    void deveReprovarProposta_ComSucesso() {
        when(propostaRepository.findById(proposta.getIdProposta())).thenReturn(Optional.of(proposta));
        when(propostaRepository.save(any(Proposta.class))).thenReturn(proposta);

        Proposta resultado = propostaService.aprovarOuReprovarProposta(proposta.getIdProposta(), false);

        assertEquals(StatusProposta.NAO_APROVADA, resultado.getStatus());
    }

    @Test
    void deveLancarExcecao_QuandoStatusNaoForAguardandoAprovacao() {
        proposta.setStatus(StatusProposta.APROVADA);

        when(propostaRepository.findById(proposta.getIdProposta())).thenReturn(Optional.of(proposta));

        assertThrows(PropostaInvalidaException.class,
                () -> propostaService.aprovarOuReprovarProposta(proposta.getIdProposta(), true));
    }

    @Test
    void deveRemoverProposta_ComSucesso() {
        when(propostaRepository.existsById(proposta.getIdProposta())).thenReturn(true);
        doNothing().when(propostaRepository).deleteById(proposta.getIdProposta());

        propostaService.removerProposta(proposta.getIdProposta());

        verify(propostaRepository, times(1)).deleteById(proposta.getIdProposta());
    }

    @Test
    void deveLancarExcecao_QuandoRemoverPropostaInexistente() {
        when(propostaRepository.existsById(any())).thenReturn(false);

        assertThrows(PropostaInvalidaException.class,
                () -> propostaService.removerProposta(UUID.randomUUID()));
    }
}
