package com.marketplace.ifba.mapper;

import com.marketplace.ifba.dto.ChatResponse;
import com.marketplace.ifba.dto.MensagemRequest;
import com.marketplace.ifba.dto.MensagemResponse;
import com.marketplace.ifba.model.Chat;
import com.marketplace.ifba.model.ChatUsuario;
import com.marketplace.ifba.model.Mensagem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ChatMapper {

    private final UserMapper userMapper;

    public ChatMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Mensagem toEntity(MensagemRequest request) {
        if (request == null) {
            return null;
        }

        Mensagem mensagem = new Mensagem();
        mensagem.setMensagem(request.mensagem());
        mensagem.setAtivo(true);

        return mensagem;
    }

    public MensagemResponse toMensagemDTO(Mensagem mensagem) {
        if (mensagem == null) {
            return null;
        }

        return new MensagemResponse(
                mensagem.getIdMensagem(),
                mensagem.getMensagem(),
                mensagem.getDataMensagem(),
                mensagem.getAtivo(),
                mensagem.getChat().getIdChat(),
                Optional.ofNullable(mensagem.getUsuarioEscritor()).map(userMapper::toDTO).orElse(null)
        );
    }

    public ChatResponse toDTO(Chat chat) {
        if (chat == null) {
            return null;
        }

        return new ChatResponse(
                chat.getIdChat(),
                chat.getDataCriacao(),
                chat.getDataEncerrado(),
                Optional.ofNullable(chat.getProjeto()).map(projeto -> projeto.getIdProjeto()).orElse(null),
                Optional.ofNullable(chat.getMensagens()).orElseGet(ArrayList::new)
                        .stream()
                        .filter(mensagem -> mensagem.getAtivo())
                        .map(this::toMensagemDTO)
                        .collect(Collectors.toList()),
                Optional.ofNullable(chat.getChatUsuarios()).orElseGet(ArrayList::new)
                        .stream()
                        .map(ChatUsuario::getUsuario)
                        .map(userMapper::toDTO)
                        .collect(Collectors.toList())
        );
    }
}