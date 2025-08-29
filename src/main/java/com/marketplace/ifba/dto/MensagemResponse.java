package com.marketplace.ifba.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record MensagemResponse(
        UUID idMensagem,
        String mensagem,
        LocalDateTime dataMensagem,
        Boolean ativo,
        UUID idChat,
        UserResponse usuarioEscritor
) {}