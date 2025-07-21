package com.marketplace.ifba.model.enums;

public enum StatusInstituicao {
    AGUARDANDO_APROVACAO("aguardando_aprovacao"),
    APROVADA("aprovado"),
    NAO_APROVADA("nao_aprovado"),
    INATIVA("inativa");

    private String statusInstituicao;

    StatusInstituicao(String statusInstituicao) {
        this.statusInstituicao = statusInstituicao;
    }

    public String getStatusInstituicao() {
        return statusInstituicao;
    }
}
