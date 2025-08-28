package com.marketplace.ifba.service;

import com.marketplace.ifba.exception.*;
import com.marketplace.ifba.model.Organizacao;
import com.marketplace.ifba.model.Solicitacao;
import com.marketplace.ifba.model.User;
import com.marketplace.ifba.model.enums.StatusOrganizacao;
import com.marketplace.ifba.model.enums.StatusSolicitacao;
import com.marketplace.ifba.repository.OrganizacaoRepository;
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

class OrganizacaoServiceTest {

    @Mock
    private OrganizacaoRepository organizacaoRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrganizacaoService organizacaoService;

    private Organizacao organizacao;
    private User usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuario = new User();
        usuario.setIdUsuario(UUID.randomUUID());

        organizacao = new Organizacao();
        organizacao.setIdOrganizacao(UUID.randomUUID());
        organizacao.setNome("Organização Teste");
        organizacao.setCnpj("12345678000199");
        organizacao.setStatus(StatusOrganizacao.AGUARDANDO_APROVACAO);
        organizacao.setUsuarioRegistro(usuario);
        organizacao.setUsuarioGerente(usuario);
        organizacao.setDataRegistro(LocalDateTime.now());
    }

    @Test
    void deveBuscarOrganizacaoPorId_ComSucesso() {
        when(organizacaoRepository.findById(organizacao.getIdOrganizacao())).thenReturn(Optional.of(organizacao));

        Organizacao resultado = organizacaoService.buscarOrganizacaoPorId(organizacao.getIdOrganizacao());

        assertNotNull(resultado);
        assertEquals("Organização Teste", resultado.getNome());
        verify(organizacaoRepository, times(1)).findById(organizacao.getIdOrganizacao());
    }

    @Test
    void deveLancarExcecao_QuandoBuscarPorIdInexistente() {
        when(organizacaoRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(OrganizacaoInvalidaException.class,
                () -> organizacaoService.buscarOrganizacaoPorId(UUID.randomUUID()));
    }

    @Test
    void deveBuscarTodasOrganizacoes() {
        when(organizacaoRepository.findAll()).thenReturn(List.of(organizacao));

        List<Organizacao> resultado = organizacaoService.buscarTodasOrganizacoes();

        assertEquals(1, resultado.size());
        assertEquals("Organização Teste", resultado.get(0).getNome());
    }

    @Test
    void deveBuscarPorNome_ComSucesso() {
        when(organizacaoRepository.findAll()).thenReturn(List.of(organizacao));

        Organizacao resultado = organizacaoService.buscarOrganizacaoPorNome("Organização Teste");

        assertNotNull(resultado);
        assertEquals("Organização Teste", resultado.getNome());
    }

    @Test
    void deveBuscarPorCnpj_ComSucesso() {
        when(organizacaoRepository.findAll()).thenReturn(List.of(organizacao));

        Organizacao resultado = organizacaoService.buscarOrganizacaoPorCnpj("12345678000199");

        assertNotNull(resultado);
        assertEquals("12345678000199", resultado.getCnpj());
    }

    @Test
    void deveBuscarUsuariosSolicitantesAssociacao() {
        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setStatus(StatusSolicitacao.ATIVA);
        solicitacao.setUserApplicant(usuario);

        organizacao.setSolicitacoes(List.of(solicitacao));

        when(organizacaoRepository.findById(organizacao.getIdOrganizacao())).thenReturn(Optional.of(organizacao));

        List<User> resultado = organizacaoService.buscarUsuariosSolicitantesAssociacao(organizacao.getIdOrganizacao());

        assertEquals(1, resultado.size());
        assertEquals(usuario.getIdUsuario(), resultado.get(0).getIdUsuario());
    }

    @Test
    void deveRegistrarOrganizacao_ComSucesso() {
        when(organizacaoRepository.findAll()).thenReturn(List.of());
        when(userRepository.findById(usuario.getIdUsuario())).thenReturn(Optional.of(usuario));
        when(organizacaoRepository.save(any(Organizacao.class))).thenReturn(organizacao);

        Organizacao resultado = organizacaoService.registrarOrganizacao(organizacao, usuario.getIdUsuario());

        assertNotNull(resultado);
        assertEquals(StatusOrganizacao.AGUARDANDO_APROVACAO, resultado.getStatus());
        verify(organizacaoRepository, times(1)).save(organizacao);
    }

    @Test
    void deveLancarExcecao_QuandoNomeDuplicado() {
        when(organizacaoRepository.findAll()).thenReturn(List.of(organizacao));

        Organizacao nova = new Organizacao();
        nova.setNome("Organização Teste");
        nova.setCnpj("99999999000188");

        assertThrows(OrganizacaoInvalidaException.class,
                () -> organizacaoService.registrarOrganizacao(nova, usuario.getIdUsuario()));
    }

    @Test
    void deveLancarExcecao_QuandoCnpjDuplicado() {
        when(organizacaoRepository.findAll()).thenReturn(List.of(organizacao));

        Organizacao nova = new Organizacao();
        nova.setNome("Outra Org");
        nova.setCnpj("12345678000199");

        assertThrows(OrganizacaoInvalidaException.class,
                () -> organizacaoService.registrarOrganizacao(nova, usuario.getIdUsuario()));
    }

    @Test
    void deveAtualizarOrganizacao_ComSucesso() {
        Organizacao nova = new Organizacao();
        nova.setNome("Novo Nome");
        nova.setSigla("NN");
        nova.setTipoOrganizacao("Associação");

        when(organizacaoRepository.findById(organizacao.getIdOrganizacao())).thenReturn(Optional.of(organizacao));
        when(organizacaoRepository.save(any(Organizacao.class))).thenReturn(organizacao);

        Organizacao resultado = organizacaoService.atualizarOrganizacao(nova, organizacao.getIdOrganizacao());

        assertEquals("Novo Nome", resultado.getNome());
        assertEquals("NN", resultado.getSigla());
    }

    @Test
    void deveAprovarOrganizacao() {
        when(organizacaoRepository.findById(organizacao.getIdOrganizacao())).thenReturn(Optional.of(organizacao));
        when(userRepository.findById(usuario.getIdUsuario())).thenReturn(Optional.of(usuario));
        when(organizacaoRepository.save(any(Organizacao.class))).thenReturn(organizacao);

        Organizacao resultado = organizacaoService.aprovaOuReprovaOrganizacao(organizacao.getIdOrganizacao(),
                usuario.getIdUsuario(), true);

        assertEquals(StatusOrganizacao.APROVADA, resultado.getStatus());
        assertNotNull(resultado.getDataAprovacao());
    }

    @Test
    void deveReprovarOrganizacao() {
        when(organizacaoRepository.findById(organizacao.getIdOrganizacao())).thenReturn(Optional.of(organizacao));
        when(userRepository.findById(usuario.getIdUsuario())).thenReturn(Optional.of(usuario));
        when(organizacaoRepository.save(any(Organizacao.class))).thenReturn(organizacao);

        Organizacao resultado = organizacaoService.aprovaOuReprovaOrganizacao(organizacao.getIdOrganizacao(),
                usuario.getIdUsuario(), false);

        assertEquals(StatusOrganizacao.NAO_APROVADA, resultado.getStatus());
    }

    @Test
    void deveRemoverOrganizacao_ComSucesso() {
        when(organizacaoRepository.existsById(organizacao.getIdOrganizacao())).thenReturn(true);
        doNothing().when(organizacaoRepository).deleteById(organizacao.getIdOrganizacao());

        organizacaoService.removerOrganizacao(organizacao.getIdOrganizacao());

        verify(organizacaoRepository, times(1)).deleteById(organizacao.getIdOrganizacao());
    }

    @Test
    void deveLancarExcecao_QuandoRemoverOrganizacaoInexistente() {
        when(organizacaoRepository.existsById(any())).thenReturn(false);

        assertThrows(OrganizacaoInvalidaException.class,
                () -> organizacaoService.removerOrganizacao(UUID.randomUUID()));
    }
}
