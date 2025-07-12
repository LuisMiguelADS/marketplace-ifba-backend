package com.marketplace.ifba.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID idUsuario,
        String nomeCompleto,
        String email,
        String telefone,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime dataRegistro,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dataNascimento,
        String biografia,
        String endereco
) {}
