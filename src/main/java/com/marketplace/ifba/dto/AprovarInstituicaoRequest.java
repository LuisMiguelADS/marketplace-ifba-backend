package com.marketplace.ifba.dto;

import java.util.UUID;

public record AprovarInstituicaoRequest(UUID idInstituicao, UUID idAdmAprovador) {
}
