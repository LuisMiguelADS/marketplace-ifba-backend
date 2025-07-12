package com.marketplace.ifba.dto;

import java.util.UUID;

public record TagResponse(
        UUID idTag,
        String nomeTag
) {
}
