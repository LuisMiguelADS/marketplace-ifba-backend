package com.marketplace.ifba.service;

import com.marketplace.ifba.exception.ChatInvalidoException;
import com.marketplace.ifba.exception.MensagemInvalidaException;
import com.marketplace.ifba.exception.UsuarioInvalidoException;
import com.marketplace.ifba.model.Chat;
import com.marketplace.ifba.model.Mensagem;
import com.marketplace.ifba.model.Projeto;
import com.marketplace.ifba.model.User;
import com.marketplace.ifba.repository.ChatRepository;
import com.marketplace.ifba.repository.MensagemRepository;
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

class ChatServiceTest {

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private MensagemRepository mensagemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ChatService chatService;

    private Chat chat;
    private User user;
    private Mensagem mensagem;
    private UUID idProjeto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        idProjeto = UUID.randomUUID();

        // Usuário
        user = new User();
        user.setIdUsuario(UUID.randomUUID());
        user.setEmail("usuario@test.com");

        // Projeto fictício para chat
        Projeto projeto = new Projeto();
        projeto.setIdProjeto(idProjeto);

        // Chat
        chat = new Chat();
        chat.setIdChat(UUID.randomUUID());
        chat.setProjeto(projeto);
        chat.setMensagens(new ArrayList<>());

        // Mensagem
        mensagem = new Mensagem();
        mensagem.setIdMensagem(UUID.randomUUID());
        mensagem.setMensagem("Mensagem Teste");
        mensagem.setAtivo(true);
        mensagem.setChat(chat);
        mensagem.setUsuarioEscritor(user);
        mensagem.setDataMensagem(LocalDateTime.now());
    }

    // ------------------- TESTES BUSCA -------------------

    @Test
    void deveBuscarChatPorId_ComSucesso() {
        when(chatRepository.findById(chat.getIdChat())).thenReturn(Optional.of(chat));

        Chat resultado = chatService.buscarChatPorId(chat.getIdChat());

        assertNotNull(resultado);
        assertEquals(chat.getIdChat(), resultado.getIdChat());
    }

    @Test
    void deveLancarExcecao_QuandoChatNaoExistirPorId() {
        when(chatRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ChatInvalidoException.class,
                () -> chatService.buscarChatPorId(UUID.randomUUID()));
    }

    @Test
    void deveBuscarChatPorProjeto_ComSucesso() {
        when(chatRepository.findAll()).thenReturn(List.of(chat));

        Chat resultado = chatService.buscarChatPorProjeto(idProjeto);

        assertNotNull(resultado);
        assertEquals(idProjeto, resultado.getProjeto().getIdProjeto());
    }

    @Test
    void deveLancarExcecao_QuandoChatNaoExistirPorProjeto() {
        when(chatRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(ChatInvalidoException.class,
                () -> chatService.buscarChatPorProjeto(UUID.randomUUID()));
    }

    @Test
    void deveBuscarTodosChats() {
        when(chatRepository.findAll()).thenReturn(List.of(chat));

        List<Chat> resultados = chatService.buscarTodosChats();

        assertEquals(1, resultados.size());
        assertEquals(chat.getIdChat(), resultados.get(0).getIdChat());
    }

    // ------------------- TESTES MENSAGENS -------------------

    @Test
    void deveEnviarMensagem_ComSucesso() {
        String conteudo = "Nova Mensagem";
        when(chatRepository.findById(chat.getIdChat())).thenReturn(Optional.of(chat));
        when(userRepository.findById(user.getIdUsuario())).thenReturn(Optional.of(user));
        when(mensagemRepository.save(any(Mensagem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Mensagem resultado = chatService.enviarMensagem(chat.getIdChat(), conteudo, user.getIdUsuario());

        assertNotNull(resultado);
        assertEquals(conteudo, resultado.getMensagem());
        assertTrue(resultado.getAtivo());
        assertEquals(chat, resultado.getChat());
        assertEquals(user, resultado.getUsuarioEscritor());
    }

    @Test
    void deveLancarExcecao_QuandoEnviarMensagem_ChatInvalido() {
        when(chatRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ChatInvalidoException.class,
                () -> chatService.enviarMensagem(UUID.randomUUID(), "Teste", user.getIdUsuario()));
    }

    @Test
    void deveLancarExcecao_QuandoEnviarMensagem_UsuarioInvalido() {
        when(chatRepository.findById(chat.getIdChat())).thenReturn(Optional.of(chat));
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(UsuarioInvalidoException.class,
                () -> chatService.enviarMensagem(chat.getIdChat(), "Teste", UUID.randomUUID()));
    }

    @Test
    void deveEditarMensagem_ComSucesso() {
        String novoConteudo = "Mensagem Editada";
        when(mensagemRepository.findById(mensagem.getIdMensagem())).thenReturn(Optional.of(mensagem));
        when(mensagemRepository.save(any(Mensagem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Mensagem resultado = chatService.editarMensagem(mensagem.getIdMensagem(), novoConteudo);

        assertEquals(novoConteudo, resultado.getMensagem());
    }

    @Test
    void deveLancarExcecao_QuandoEditarMensagemInexistente() {
        when(mensagemRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(MensagemInvalidaException.class,
                () -> chatService.editarMensagem(UUID.randomUUID(), "Teste"));
    }

    @Test
    void deveLancarExcecao_QuandoEditarMensagemInativa() {
        mensagem.setAtivo(false);
        when(mensagemRepository.findById(mensagem.getIdMensagem())).thenReturn(Optional.of(mensagem));

        assertThrows(MensagemInvalidaException.class,
                () -> chatService.editarMensagem(mensagem.getIdMensagem(), "Teste"));
    }
}
