package com.marketplace.ifba.model.enums;

public enum StatusProposta {
    ATIVA("ativa"),
    INATIVA("inativa");

    private String statusProposta;

    StatusProposta(String statusProposta) {
        this.statusProposta = statusProposta;
    }

    public String getStatusProposta() {
        return statusProposta;
    }
}
