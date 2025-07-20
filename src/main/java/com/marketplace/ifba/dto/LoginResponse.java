package com.marketplace.ifba.dto;

public record LoginResponse(String token, UserResponse user) {
}
