package com.marketplace.ifba.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.marketplace.ifba.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record UserRequest(
        @NotBlank(message = "O nome é obrigatório.")
        String nomeCompleto,

        @NotNull(message = "A função do usuário é obrigatória.")
        UserRole role,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "E-mail inválido")
        String email,

        @NotBlank(message = "O telefone é obrigatório.")
        String telefone,

        @NotBlank(message = "A senha é obrigarória")
        String password,

        @NotBlank(message = "O CPF é obrigatório.")
        @CPF
        String cpf,

        @NotNull(message = "A data de nascimento é obrigatória.")
        @Past(message = "A data de nascimento deve ser no passado.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dataNascimento,

        @NotBlank(message = "A biografia é obrigatória.")
        String biografia,

        @NotBlank(message = "A foto de perfil é obrigatória.")
        String fotoPerfilURL,

        @NotBlank(message = "O endereço é obrigatório.")
        String endereco,

        @NotBlank(message = "A instituição é obrigatória.")
        String instituicao,

        @NotBlank(message = "A organização é obrigatória.")
        String organizacao
) {}
