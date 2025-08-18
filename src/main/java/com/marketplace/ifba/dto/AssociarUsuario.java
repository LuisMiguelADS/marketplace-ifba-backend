package com.marketplace.ifba.dto;

import java.util.UUID;

public record AssociarUsuario(UUID idUsuario, UUID idEntidade, Boolean decisao) {
}
