package com.marketplace.ifba.model.enums;

public enum StatusSolicitacao {
    ATIVA("ativa"),
    REJEITADA("rejeitada"),
    CANCELADA("cancelada"),
    APROVADA("aprovada");


    private String statusSolicitacao;

    StatusSolicitacao(String statusSolicitacao) {
        this.statusSolicitacao = statusSolicitacao;
    }

    public String getStatusSolicitacao() {
        return statusSolicitacao;
    }
}
