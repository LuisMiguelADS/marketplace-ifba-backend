package com.marketplace.ifba.dto;

import com.marketplace.ifba.model.enums.UserRole;

public record RegisterDTO(String email, String password, UserRole role) {
}
