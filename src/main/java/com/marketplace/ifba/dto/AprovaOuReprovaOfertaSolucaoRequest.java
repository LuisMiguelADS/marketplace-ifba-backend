package com.marketplace.ifba.dto;

import java.util.UUID;

public record AprovaOuReprovaOfertaSolucaoRequest(
        UUID idOfertaSolucao,
        Boolean decisao
) {
}
