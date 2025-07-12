package com.marketplace.ifba.model.enums;

public enum StatusGrupoPesquisa {
    ATIVO("ativo"),
    INATIVO("inativo"),
    DESENVOLVENDO("desenvolvendo");

    private String statusGrupo;

    StatusGrupoPesquisa(String statusGrupo) {
        this.statusGrupo = statusGrupo;
    }

    public String getStatusGrupo() {
        return statusGrupo;
    }
}
