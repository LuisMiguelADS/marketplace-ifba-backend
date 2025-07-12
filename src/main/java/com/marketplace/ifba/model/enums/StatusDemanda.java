package com.marketplace.ifba.model.enums;

public enum StatusDemanda {
    AGUARDANDO_APROVACAO("aguardando_aprovacao"),
    NAO_APROVADO("nao_aprovado"),
    AGUARDANDO_PROPOSTA("aguardando_proposta"),
    EXCLUIDA("excluida"),
    INATIVA("inativa"),
    FINALIZADA("finalizada");

    private String role;

    StatusDemanda(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
