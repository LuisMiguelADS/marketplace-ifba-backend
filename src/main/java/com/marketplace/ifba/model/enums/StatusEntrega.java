package com.marketplace.ifba.model.enums;

public enum StatusEntrega {
    SOLICITADA("solicitada"),
    ENTREGUE("entregue"),
    CANCELADA("cancelada");

    private String statusEntrega;

    StatusEntrega(String statusEntrega) {
        this.statusEntrega = statusEntrega;
    }

    public String getStatusEntrega() {
        return statusEntrega;
    }

}
