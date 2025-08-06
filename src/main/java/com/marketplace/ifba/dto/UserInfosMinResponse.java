package com.marketplace.ifba.dto;

import java.util.UUID;

public record UserInfosMinResponse(UUID idUsuario, String nome, String email, String telefone) {
}
