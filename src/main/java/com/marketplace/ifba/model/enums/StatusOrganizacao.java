package com.marketplace.ifba.model.enums;

public enum StatusOrganizacao {
    AGUARDANDO_APROVACAO("aguardando_aprovacao"),
    APROVADA("aprovado"),
    NAO_APROVADA("nao_aprovado"),
    INATIVA("inativa");

    private String statusOrganizacao;

    StatusOrganizacao(String statusOrganizacao) {
        this.statusOrganizacao = statusOrganizacao;
    }

    public String getStatusOrganizacao() {
        return statusOrganizacao;
    }
}
