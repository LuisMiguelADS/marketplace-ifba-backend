package com.marketplace.ifba.model.enums;

public enum StatusProposta {
    ATIVA("ativa"),
    INATIVA("inativa"),
    REJEITADA("rejeitada"),
    CANCELADA("cancelada"),
    CONCLUIDA("concluida"),
    PENDENTE_APROVACAO("pendente_aprovacao"),;


    private String statusProposta;

    StatusProposta(String statusProposta) {
        this.statusProposta = statusProposta;
    }

    public String getStatusProposta() {
        return statusProposta;
    }
}
