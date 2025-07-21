package com.marketplace.ifba.model.enums;

public enum StatusOfertaSolucao {
    ENVIADA("enviada"),
    APROVADA("aprovada"),
    NAO_APROVADA("nao_aprovada"),
    EXCLUIDA("excluida");

    private String statusOfertaSolucao;

    StatusOfertaSolucao(String statusOfertaSolucao) {
        this.statusOfertaSolucao = statusOfertaSolucao;
    }

    public String getStatusOfertaSolucao() {
        return statusOfertaSolucao;
    }

    public static StatusOfertaSolucao fromString(String statusOfertaSolucao) {
        for (StatusOfertaSolucao status : StatusOfertaSolucao.values()) {
            if (status.statusOfertaSolucao.equalsIgnoreCase(statusOfertaSolucao)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Nenhum status de oferta solução com: " + statusOfertaSolucao);
    }
}
