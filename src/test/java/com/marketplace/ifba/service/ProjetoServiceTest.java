package com.marketplace.ifba.service;

import com.marketplace.ifba.exception.*;
import com.marketplace.ifba.model.*;
import com.marketplace.ifba.model.enums.StatusEntrega;
import com.marketplace.ifba.model.enums.StatusProjeto;
import com.marketplace.ifba.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProjetoServiceTest {

        @Mock
        private ProjetoRepository projetoRepository;
        @Mock
        private OrganizacaoRepository organizacaoRepository;
        @Mock
        private InstituicaoRepository instituicaoRepository;
        @Mock
        private DemandaRepository demandaRepository;
        @Mock
        private OfertaSolucaoRepository ofertaSolucaoRepository;
        @Mock
        private GrupoPesquisaRepository grupoPesquisaRepository;

        @InjectMocks
        private ProjetoService projetoService;

        private Projeto projeto;
        private UUID projetoId;

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
                projetoId = UUID.randomUUID();

                projeto = new Projeto();
                projeto.setIdProjeto(projetoId);
                projeto.setNome("Projeto Teste");
                projeto.setStatus(StatusProjeto.DESENVOLVENDO);

                Demanda demanda = new Demanda();
                demanda.setIdDemanda(UUID.randomUUID());
                projeto.setDemanda(demanda);

                GrupoPesquisa grupoPesquisa = new GrupoPesquisa();
                grupoPesquisa.setIdGrupoPesquisa(UUID.randomUUID());

                grupoPesquisa.setUsuarios(Collections.emptyList());
                projeto.setGrupoPesquisa(grupoPesquisa);

                Instituicao instituicao = new Instituicao();
                instituicao.setIdInstituicao(UUID.randomUUID());
                projeto.setInstituicao(instituicao);

                Organizacao organizacao = new Organizacao();
                organizacao.setIdOrganizacao(UUID.randomUUID());
                projeto.setOrganizacao(organizacao);
        }

        @Test
        void deveBuscarProjetoPorIdComSucesso() {
                when(projetoRepository.findById(projetoId)).thenReturn(Optional.of(projeto));

                Projeto result = projetoService.buscarProjetoPorId(projetoId);

                assertNotNull(result);
                assertEquals(projeto.getNome(), result.getNome());
                verify(projetoRepository, times(1)).findById(projetoId);
        }

        @Test
        void deveLancarExcecaoAoBuscarProjetoPorIdNaoExistente() {
                when(projetoRepository.findById(projetoId)).thenReturn(Optional.empty());

                assertThrows(ProjetoInvalidoException.class,
                                () -> projetoService.buscarProjetoPorId(projetoId));
        }

        @Test
        void deveBuscarProjetoPorNomeComSucesso() {
                when(projetoRepository.findByNome("Projeto Teste")).thenReturn(Optional.of(projeto));

                Projeto result = projetoService.buscarProjetoPorNome("Projeto Teste");

                assertEquals("Projeto Teste", result.getNome());
        }

        @Test
        void deveLancarExcecaoAoBuscarProjetoPorNomeNaoExistente() {
                when(projetoRepository.findByNome("Inexistente")).thenReturn(Optional.empty());

                assertThrows(ProjetoInvalidoException.class,
                                () -> projetoService.buscarProjetoPorNome("Nao existe"));
        }

        @Test
        void deveAtualizarNomeProjetoComSucesso() {
                String novoNome = "Projeto Atualizado";

                when(projetoRepository.findById(projetoId)).thenReturn(Optional.of(projeto));
                when(projetoRepository.findByNome(novoNome)).thenReturn(Optional.empty());
                when(projetoRepository.save(any(Projeto.class))).thenReturn(projeto);

                Projeto result = projetoService.atualizarNomeProjeto(projetoId, novoNome);

                assertEquals(novoNome, result.getNome());
                verify(projetoRepository).save(projeto);
        }

        @Test
        void deveLancarExcecaoAoAtualizarNomeProjetoDuplicado() {
                String novoNome = "Duplicado";
                Projeto outroProjeto = new Projeto();
                outroProjeto.setIdProjeto(UUID.randomUUID());
                outroProjeto.setNome(novoNome);

                when(projetoRepository.findById(projetoId)).thenReturn(Optional.of(projeto));
                when(projetoRepository.findByNome(novoNome)).thenReturn(Optional.of(outroProjeto));

                assertThrows(ProjetoInvalidoException.class,
                                () -> projetoService.atualizarNomeProjeto(projetoId, novoNome));
        }

        @Test
        void deveAtualizarStatusProjetoComSucesso() {
                when(projetoRepository.findById(projetoId)).thenReturn(Optional.of(projeto));
                when(projetoRepository.save(any(Projeto.class))).thenReturn(projeto);

                Projeto result = projetoService.atualizarStatusProjeto(projetoId, StatusProjeto.FINALIZADO);

                assertEquals(StatusProjeto.FINALIZADO, result.getStatus());
        }

        @Test
        void deveLancarExcecaoAoAtualizarStatusDeProjetoCancelado() {
                projeto.setStatus(StatusProjeto.CANCELADO);
                when(projetoRepository.findById(projetoId)).thenReturn(Optional.of(projeto));

                assertThrows(ProjetoInvalidoException.class,
                                () -> projetoService.atualizarStatusProjeto(projetoId, StatusProjeto.DESENVOLVENDO));
        }

        @Test
        void deveRemoverProjetoComSucesso() {
                when(projetoRepository.existsById(projetoId)).thenReturn(true);

                projetoService.removerProjeto(projetoId);

                verify(projetoRepository).deleteById(projetoId);
        }

        @Test
        void deveLancarExcecaoAoRemoverProjetoInexistente() {
                when(projetoRepository.existsById(projetoId)).thenReturn(false);

                assertThrows(ProjetoInvalidoException.class,
                                () -> projetoService.removerProjeto(projetoId));
        }

        @Test
        void deveAdicionarEntregaComSucesso() {
                Entrega entrega = new Entrega();
                UUID idOrgSolicitante = projeto.getOrganizacao().getIdOrganizacao();
                UUID idGrupoPesqSolicitado = projeto.getGrupoPesquisa().getIdGrupoPesquisa();

                when(projetoRepository.findById(projetoId)).thenReturn(Optional.of(projeto));
                when(organizacaoRepository.findById(idOrgSolicitante))
                                .thenReturn(Optional.of(projeto.getOrganizacao()));
                when(grupoPesquisaRepository.findById(idGrupoPesqSolicitado))
                                .thenReturn(Optional.of(projeto.getGrupoPesquisa()));

                Entrega result = projetoService.adicionarEntrega(
                                projetoId, entrega, idOrgSolicitante, null, null, idGrupoPesqSolicitado);

                assertNotNull(result.getProjeto());
                assertEquals(StatusEntrega.SOLICITADA, result.getStatus());
        }

        // ---------- FILTROS ----------
        @Test
        void deveBuscarProjetosPorGrupoPesquisa() {
                when(grupoPesquisaRepository.findById(projeto.getGrupoPesquisa().getIdGrupoPesquisa()))
                                .thenReturn(Optional.of(projeto.getGrupoPesquisa()));

                when(projetoRepository.findByGrupoPesquisa(projeto.getGrupoPesquisa()))
                                .thenReturn(List.of(projeto));

                List<Projeto> result = projetoService
                                .buscarProjetosPorGrupoPesquisa(projeto.getGrupoPesquisa().getIdGrupoPesquisa());

                assertFalse(result.isEmpty());
                assertEquals(1, result.size());
                assertEquals(projeto.getIdProjeto(), result.get(0).getIdProjeto());
        }

        @Test
        void deveLancarExcecaoAoBuscarProjetosPorGrupoPesquisaInexistente() {
                UUID idGrupo = UUID.randomUUID();
                when(grupoPesquisaRepository.findById(idGrupo)).thenReturn(Optional.empty());

                assertThrows(GrupoPesquisaInvalidoException.class,
                                () -> projetoService.buscarProjetosPorGrupoPesquisa(idGrupo));
        }

        @Test
        void deveBuscarProjetosPorInstituicao() {
                when(instituicaoRepository.findById(projeto.getInstituicao().getIdInstituicao()))
                                .thenReturn(Optional.of(projeto.getInstituicao()));
                when(projetoRepository.findByInstituicao(projeto.getInstituicao()))
                                .thenReturn(List.of(projeto));

                List<Projeto> result = projetoService
                                .buscarProjetosPorInstituicao(projeto.getInstituicao().getIdInstituicao());

                assertEquals(1, result.size());
                assertEquals(projeto.getNome(), result.get(0).getNome());
        }

        @Test
        void deveLancarExcecaoAoBuscarProjetosPorInstituicaoInexistente() {
                UUID idInst = UUID.randomUUID();
                when(instituicaoRepository.findById(idInst)).thenReturn(Optional.empty());

                assertThrows(InstituicaoInvalidaException.class,
                                () -> projetoService.buscarProjetosPorInstituicao(idInst));
        }

        @Test
        void deveBuscarProjetosPorOrganizacao() {
                when(organizacaoRepository.findById(projeto.getOrganizacao().getIdOrganizacao()))
                                .thenReturn(Optional.of(projeto.getOrganizacao()));
                when(projetoRepository.findByOrganizacao(projeto.getOrganizacao()))
                                .thenReturn(List.of(projeto));

                List<Projeto> result = projetoService
                                .buscarProjetosPorOrganizacao(projeto.getOrganizacao().getIdOrganizacao());

                assertEquals(1, result.size());
                assertEquals(projeto.getIdProjeto(), result.get(0).getIdProjeto());
        }

        @Test
        void deveLancarExcecaoAoBuscarProjetosPorOrganizacaoInexistente() {
                UUID idOrg = UUID.randomUUID();
                when(organizacaoRepository.findById(idOrg)).thenReturn(Optional.empty());

                assertThrows(OrganizacaoInvalidaException.class,
                                () -> projetoService.buscarProjetosPorOrganizacao(idOrg));
        }

        @Test
        void deveLancarExcecaoQuandoOrganizacaoNaoEncontrada() {
                UUID idOrg = UUID.randomUUID();

                when(projetoRepository.findAll()).thenReturn(Collections.emptyList());
                when(organizacaoRepository.findById(idOrg)).thenReturn(Optional.empty());

                assertThrows(OrganizacaoInvalidaException.class,
                                () -> projetoService.registrarProjeto(
                                                new Projeto(),
                                                idOrg,
                                                projeto.getInstituicao().getIdInstituicao(),
                                                projeto.getDemanda().getIdDemanda(),
                                                null,
                                                projeto.getGrupoPesquisa().getIdGrupoPesquisa()));
        }

        @Test
        void deveLancarExcecaoQuandoInstituicaoNaoEncontrada() {
                UUID idInst = UUID.randomUUID();

                when(projetoRepository.findAll()).thenReturn(Collections.emptyList());
                when(organizacaoRepository.findById(projeto.getOrganizacao().getIdOrganizacao()))
                                .thenReturn(Optional.of(projeto.getOrganizacao()));
                when(instituicaoRepository.findById(idInst)).thenReturn(Optional.empty());

                assertThrows(InstituicaoInvalidaException.class,
                                () -> projetoService.registrarProjeto(
                                                new Projeto(),
                                                projeto.getOrganizacao().getIdOrganizacao(),
                                                idInst,
                                                projeto.getDemanda().getIdDemanda(),
                                                null,
                                                projeto.getGrupoPesquisa().getIdGrupoPesquisa()));
        }

        @Test
        void deveLancarExcecaoQuandoDemandaNaoEncontrada() {
                UUID idDemanda = UUID.randomUUID();

                when(projetoRepository.findAll()).thenReturn(Collections.emptyList());
                when(organizacaoRepository.findById(projeto.getOrganizacao().getIdOrganizacao()))
                                .thenReturn(Optional.of(projeto.getOrganizacao()));
                when(instituicaoRepository.findById(projeto.getInstituicao().getIdInstituicao()))
                                .thenReturn(Optional.of(projeto.getInstituicao()));
                when(demandaRepository.findById(idDemanda)).thenReturn(Optional.empty());

                assertThrows(DemandaInvalidaException.class,
                                () -> projetoService.registrarProjeto(
                                                new Projeto(),
                                                projeto.getOrganizacao().getIdOrganizacao(),
                                                projeto.getInstituicao().getIdInstituicao(),
                                                idDemanda,
                                                null,
                                                projeto.getGrupoPesquisa().getIdGrupoPesquisa()));
        }

        @Test
        void deveLancarExcecaoQuandoGrupoPesquisaNaoEncontrado() {
                UUID idGrupo = UUID.randomUUID();

                when(projetoRepository.findAll()).thenReturn(Collections.emptyList());
                when(organizacaoRepository.findById(projeto.getOrganizacao().getIdOrganizacao()))
                                .thenReturn(Optional.of(projeto.getOrganizacao()));
                when(instituicaoRepository.findById(projeto.getInstituicao().getIdInstituicao()))
                                .thenReturn(Optional.of(projeto.getInstituicao()));
                when(demandaRepository.findById(projeto.getDemanda().getIdDemanda()))
                                .thenReturn(Optional.of(projeto.getDemanda()));
                when(grupoPesquisaRepository.findById(idGrupo)).thenReturn(Optional.empty());

                assertThrows(GrupoPesquisaInvalidoException.class,
                                () -> projetoService.registrarProjeto(
                                                new Projeto(),
                                                projeto.getOrganizacao().getIdOrganizacao(),
                                                projeto.getInstituicao().getIdInstituicao(),
                                                projeto.getDemanda().getIdDemanda(),
                                                null,
                                                idGrupo));
        }

        @Test
        void deveLancarExcecaoQuandoOfertaNaoEncontrada() {
                UUID idOferta = UUID.randomUUID();

                when(projetoRepository.findAll()).thenReturn(Collections.emptyList());
                when(organizacaoRepository.findById(projeto.getOrganizacao().getIdOrganizacao()))
                                .thenReturn(Optional.of(projeto.getOrganizacao()));
                when(instituicaoRepository.findById(projeto.getInstituicao().getIdInstituicao()))
                                .thenReturn(Optional.of(projeto.getInstituicao()));
                when(demandaRepository.findById(projeto.getDemanda().getIdDemanda()))
                                .thenReturn(Optional.of(projeto.getDemanda()));
                when(grupoPesquisaRepository.findById(projeto.getGrupoPesquisa().getIdGrupoPesquisa()))
                                .thenReturn(Optional.of(projeto.getGrupoPesquisa()));
                when(ofertaSolucaoRepository.findById(idOferta)).thenReturn(Optional.empty());

                assertThrows(OfertaSolucaoInvalidaException.class,
                                () -> projetoService.registrarProjeto(
                                                new Projeto(),
                                                projeto.getOrganizacao().getIdOrganizacao(),
                                                projeto.getInstituicao().getIdInstituicao(),
                                                projeto.getDemanda().getIdDemanda(),
                                                idOferta,
                                                projeto.getGrupoPesquisa().getIdGrupoPesquisa()));
        }

        @Test
        void deveLancarExcecaoAoAtualizarStatusDeProjetoFinalizado() {
                projeto.setStatus(StatusProjeto.FINALIZADO);
                when(projetoRepository.findById(projetoId)).thenReturn(Optional.of(projeto));

                assertThrows(ProjetoInvalidoException.class,
                                () -> projetoService.atualizarStatusProjeto(projetoId, StatusProjeto.DESENVOLVENDO));
        }

        @Test
        void deveLancarExcecaoAoAtualizarStatusParaMesmoStatusDesenvolvendo() {
                projeto.setStatus(StatusProjeto.DESENVOLVENDO);
                when(projetoRepository.findById(projetoId)).thenReturn(Optional.of(projeto));

                assertThrows(ProjetoInvalidoException.class,
                                () -> projetoService.atualizarStatusProjeto(projetoId, StatusProjeto.DESENVOLVENDO));
        }

        @Test
        void deveLancarExcecaoQuandoSolicitanteInvalidoNaEntrega() {
                Entrega entrega = new Entrega();
                when(projetoRepository.findById(projetoId)).thenReturn(Optional.of(projeto));

                assertThrows(ProjetoInvalidoException.class,
                                () -> projetoService.adicionarEntrega(projetoId, entrega,
                                                projeto.getOrganizacao().getIdOrganizacao(),
                                                projeto.getGrupoPesquisa().getIdGrupoPesquisa(),
                                                null, UUID.randomUUID()));
        }

        @Test
        void deveLancarExcecaoQuandoSolicitadoInvalidoNaEntrega() {
                Entrega entrega = new Entrega();
                when(projetoRepository.findById(projetoId)).thenReturn(Optional.of(projeto));

                assertThrows(ProjetoInvalidoException.class,
                                () -> projetoService.adicionarEntrega(projetoId, entrega,
                                                projeto.getOrganizacao().getIdOrganizacao(),
                                                null,
                                                projeto.getOrganizacao().getIdOrganizacao(),
                                                projeto.getGrupoPesquisa().getIdGrupoPesquisa()));
        }

        @Test
        void deveLancarExcecaoQuandoOrganizacaoSolicitanteNaoEncontrada() {
                Entrega entrega = new Entrega();
                UUID idOrgSolic = UUID.randomUUID();
                when(projetoRepository.findById(projetoId)).thenReturn(Optional.of(projeto));
                when(organizacaoRepository.findById(idOrgSolic)).thenReturn(Optional.empty());

                assertThrows(OrganizacaoInvalidaException.class,
                                () -> projetoService.adicionarEntrega(projetoId, entrega,
                                                idOrgSolic, null, null,
                                                projeto.getGrupoPesquisa().getIdGrupoPesquisa()));
        }

        @Test
        void deveBuscarEntregasPorProjetoComSucesso() {
                Entrega entrega = new Entrega();
                entrega.setProjeto(projeto);
                projeto.setEntregas(List.of(entrega));

                when(projetoRepository.findById(projetoId)).thenReturn(Optional.of(projeto));

                List<Entrega> entregas = projetoService.buscarEntregasPorProjeto(projetoId);

                assertEquals(1, entregas.size());
                assertEquals(projeto, entregas.get(0).getProjeto());
        }

        @Test
        void deveRetornarListaVaziaQuandoProjetoNaoPossuiEntregas() {
                projeto.setEntregas(null);
                when(projetoRepository.findById(projetoId)).thenReturn(Optional.of(projeto));

                List<Entrega> entregas = projetoService.buscarEntregasPorProjeto(projetoId);

                assertTrue(entregas.isEmpty());
        }

}
