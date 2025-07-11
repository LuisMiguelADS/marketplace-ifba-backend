package com.marketplace.ifba.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.marketplace.ifba.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Data
public class UserRequest {

    @NotBlank(message = "O nome é obrigatório.")
    private String nomeCompleto;

    @NotNull(message = "A função do usuário é obrigatória.")
    private UserRole role;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "E-mail inválido")
    private String email;

    @NotBlank(message = "O telefone é obrigatório.")
    private String telefone;

    @NotBlank(message = "A senha é obrigarória")
    private String password;

    @NotBlank(message = "O CPF é obrigatório.")
    @CPF
    private String cpf;

    @NotNull(message = "A data de nascimento é obrigatória.")
    @Past(message = "A data de nascimento deve ser no passado.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;

    @NotBlank(message = "A biografia é obrigatória.")
    private String biografia;

    @NotBlank(message = "A foto de perfil é obrigatória.")
    private String fotoPerfilURL;

    @NotBlank(message = "O endereço é obrigatório.")
    private String endereco;

    @NotBlank(message = "A instituição é obrigatória.")
    private String instituicao;

    @NotBlank(message = "A organização é obrigatória.")
    private String organizacao;
}