package com.marketplace.ifba.model.enums;

public enum StatusOfertaSolucao {
    AGUARDANDO_APROVACAO("aguardando-aprovacao"),
    APROVADA("aprovada"),
    REPROVADA("reprovada"),
    EXCLUIDA("excluida"),
    ACEITA("aceita");

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
