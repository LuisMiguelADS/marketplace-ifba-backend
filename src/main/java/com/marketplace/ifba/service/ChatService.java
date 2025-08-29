package com.marketplace.ifba.service;

import com.marketplace.ifba.exception.ChatInvalidoException;
import com.marketplace.ifba.exception.MensagemInvalidaException;
import com.marketplace.ifba.exception.UsuarioInvalidoException;
import com.marketplace.ifba.model.Chat;
import com.marketplace.ifba.model.Mensagem;
import com.marketplace.ifba.model.User;
import com.marketplace.ifba.repository.ChatRepository;
import com.marketplace.ifba.repository.MensagemRepository;
import com.marketplace.ifba.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final MensagemRepository mensagemRepository;
    private final UserRepository userRepository;

    public ChatService(ChatRepository chatRepository, MensagemRepository mensagemRepository, UserRepository userRepository) {
        this.chatRepository = chatRepository;
        this.mensagemRepository = mensagemRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Chat buscarChatPorId(UUID idChat) {
        return chatRepository.findById(idChat)
                .orElseThrow(() -> new ChatInvalidoException("Chat não encontrado com ID"));
    }

    @Transactional(readOnly = true)
    public Chat buscarChatPorProjeto(UUID idProjeto) {
        return chatRepository.findAll().stream()
                .filter(chat -> chat.getProjeto().getIdProjeto().equals(idProjeto))
                .findFirst()
                .orElseThrow(() -> new ChatInvalidoException("Chat não encontrado para o projeto via ID:"));
    }

    @Transactional(readOnly = true)
    public List<Chat> buscarTodosChats() {
        return chatRepository.findAll();
    }

    @Transactional
    public Mensagem enviarMensagem(UUID idChat, String conteudoMensagem, UUID idUsuario) {
        Chat chat = chatRepository.findById(idChat)
                .orElseThrow(() -> new ChatInvalidoException("Chat não encontrado com o ID"));

        User usuario = userRepository.findById(idUsuario)
                .orElseThrow(() -> new UsuarioInvalidoException("Usuário não encontrado com o ID"));

        Mensagem mensagem = new Mensagem();
        mensagem.setMensagem(conteudoMensagem);
        mensagem.setDataMensagem(LocalDateTime.now());
        mensagem.setAtivo(true);
        mensagem.setChat(chat);
        mensagem.setUsuarioEscritor(usuario);

        return mensagemRepository.save(mensagem);
    }

    @Transactional
    public Mensagem editarMensagem(UUID idMensagem, String novoConteudo) {
        Mensagem mensagem = mensagemRepository.findById(idMensagem)
                .orElseThrow(() -> new MensagemInvalidaException("Mensagem não encontrada com ID"));

        if (!mensagem.getAtivo()) {
            throw new MensagemInvalidaException("Não é possível editar uma mensagem inativa");
        }

        mensagem.setMensagem(novoConteudo);
        return mensagemRepository.save(mensagem);
    }
}