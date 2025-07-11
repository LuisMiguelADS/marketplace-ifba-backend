package com.marketplace.ifba.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record InstituicaoRequest(
        @NotBlank(message = "O nome da instituição é obrigatório.")
        String nome,
        @NotBlank(message = "A sigla da instituição é obrigatória.")
        String sigla,
        @NotBlank(message = "O CNPJ da instituição é obrigatório.")
        String cnpj,
        @NotBlank(message = "O tipo da instituição é obrigatório.")
        String tipoInstituicao,
        @NotBlank(message = "O setor da instituição é obrigatório.")
        String setor,
        @Pattern(regexp = "^\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$",
                message = "Telefone inválido, formato desejado: (00) 0000-0000 ou (00) 00000-0000")
        String telefone,
        @Pattern(regexp = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]$",
                message = "URL do site inválida.")
        String site,
        String logoURL,
        String descricao
) {
    public InstituicaoRequest {
        if (telefone != null && telefone.isBlank()) {
            telefone = null;
        }
        if (site != null && site.isBlank()) {
            site = null;
        }
        if (logoURL != null && logoURL.isBlank()) {
            logoURL = null;
        }
        if (descricao != null && descricao.isBlank()) {
            descricao = null;
        }
    }
}
