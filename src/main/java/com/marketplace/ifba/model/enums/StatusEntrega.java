package com.marketplace.ifba.model.enums;

public enum StatusEntrega {
    EM_ANALISE("em_analise"),
    SOLICITADA("solicitada"),
    ENTREGUE("entregue"),
    ACEITA("aceita"),
    REJEITADA("rejeitada"),
    CANCELADA("cancelada");

    private String statusEntrega;

    StatusEntrega(String statusEntrega) {
        this.statusEntrega = statusEntrega;
    }

    public String getStatusEntrega() {
        return statusEntrega;
    }

}
