package com.marketplace.ifba.model.enums;

public enum StatusProjeto {
    DESENVOLVENDO("desenvolvendo"),
    PAUSADO("pausado"),
    FINALIZADO("finalizado");

    private String statusProjeto;

    StatusProjeto(String statusProjeto) {
        this.statusProjeto = statusProjeto;
    }

    public String getstatusProjeto() {
        return statusProjeto;
    }
}
