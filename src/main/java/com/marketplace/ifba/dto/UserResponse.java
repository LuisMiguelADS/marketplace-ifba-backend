package com.marketplace.ifba.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.marketplace.ifba.model.enums.UserRole;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UserResponse(
        UUID idUsuario,
        String nomeCompleto,
        UserRole role,
        String email,
        String telefone,
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate dataRegistro,
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate dataNascimento,
        String biografia,
        List<LocalDateTime> conexoes,
        UUID idOrganizacao,
        UUID idInstituicao
) {}
