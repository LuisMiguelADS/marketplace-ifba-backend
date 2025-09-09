package com.marketplace.ifba.service;

import com.marketplace.ifba.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        // injeta manualmente o valor do secret (como se viesse do
        // application.properties)
        ReflectionTestUtils.setField(tokenService, "secret", "my-secret-key");
    }

    @Test
    void deveGerarToken_ComSucesso() {
        User user = new User();
        user.setEmail("usuario@test.com");

        String token = tokenService.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void deveValidarToken_ComSucesso() {
        User user = new User();
        user.setEmail("usuario@test.com");

        String token = tokenService.generateToken(user);
        String subject = tokenService.validateToken(token);

        assertEquals("usuario@test.com", subject);
    }

    @Test
    void deveRetornarVazio_QuandoTokenInvalido() {
        String subject = tokenService.validateToken("token-invalido");

        assertEquals("", subject);
    }
}
