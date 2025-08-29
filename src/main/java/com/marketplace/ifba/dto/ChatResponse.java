package com.marketplace.ifba.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ChatResponse(
        UUID idChat,
        LocalDateTime dataCriacao,
        LocalDateTime dataEncerrado,
        UUID idProjeto,
        List<MensagemResponse> mensagens,
        List<UserResponse> participantes
) {}