package com.marketplace.ifba.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.br.CNPJ;

import java.util.UUID;

public record OrganizacaoRequest(
        @NotBlank(message = "O nome da organização é obrigatório.")
        @Size(max = 30, message = "O nome não pode exceder 30 caracteres.")
        String nome,

        @Size(max = 10, message = "A sigla não pode exceder 10 caracteres.")
        String sigla,

        @CNPJ(message = "CNPJ inválido.")
        @NotBlank(message = "O CNPJ é obrigatório.")
        String cnpj,

        @NotBlank(message = "O tipo de organização é obrigatório.")
        @Size(max = 30, message = "O tipo de organização não pode exceder 30 caracteres.")
        String tipoOrganizacao,

        @Size(max = 50, message = "O setor não pode exceder 50 caracteres.")
        String setor,

        @Size(max = 15, message = "O telefone não pode exceder 15 caracteres.")
        String telefone,

        @URL(message = "URL do site inválida.")
        @Size(max = 255, message = "A URL do site não pode exceder 255 caracteres.")
        String site,

        @URL(message = "URL do logo inválida.")
        @Size(max = 500, message = "A URL do logo não pode exceder 500 caracteres.")
        String logoURL,

        @Size(max = 2000, message = "A descrição não pode exceder 2000 caracteres.")
        String descricao,
        UUID idUsuarioRegistrador
) {}
