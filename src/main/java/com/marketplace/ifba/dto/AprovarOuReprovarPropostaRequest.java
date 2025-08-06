package com.marketplace.ifba.dto;

import java.util.UUID;

public record AprovarOuReprovarPropostaRequest(
        UUID idProposta,
        Boolean decisao
) {
}
