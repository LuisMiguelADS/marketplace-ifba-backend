package com.marketplace.ifba.dto;

import com.marketplace.ifba.model.enums.UserRole;

import java.time.LocalDate;

public record RegisterDTO(String nomeCompleto,
                          UserRole role,
                          String email,
                          String telefone,
                          String password,
                          String cpf,
                          LocalDate dataNascimento,
                          String biografia,
                          String fotoPerfilURL,
                          String endereco,
                          String instituicao,
                          String organizacao) {
}
