package com.marketplace.ifba.model.enums;

public enum StatusProposta {
    INATIVA("inativa"),
    NAO_APROVADA("nao-aprovada"),
    APROVADA("aprovada"),
    AGUARDANDO_APROVACAO("aguardando_aprovacao"),;

    private String statusProposta;

    StatusProposta(String statusProposta) {
        this.statusProposta = statusProposta;
    }

    public String getStatusProposta() {
        return statusProposta;
    }
}
