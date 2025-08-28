package com.marketplace.ifba.service;

import com.marketplace.ifba.model.User;
import com.marketplace.ifba.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorizationServiceTest {

    private UserRepository userRepository;
    private AuthorizationService authorizationService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        authorizationService = new AuthorizationService(userRepository);
    }

    @Test
    void deveRetornarUserDetailsQuandoUsuarioExistir() {
        // Arrange
        User user = new User();
        user.setEmail("email@teste.com");
        user.setPassword("123456");

        when(userRepository.findAll()).thenReturn(List.of(user));

        // Act
        UserDetails result = authorizationService.loadUserByUsername("email@teste.com");

        // Assert
        assertNotNull(result);
        assertEquals("email@teste.com", result.getUsername());
        assertEquals("123456", result.getPassword());
        verify(userRepository).findAll();
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {
        // Arrange
        User user = new User();
        user.setEmail("outro@teste.com");
        user.setPassword("123");

        when(userRepository.findAll()).thenReturn(List.of(user));

        // Act & Assert
        assertThrows(UsernameNotFoundException.class,
                () -> authorizationService.loadUserByUsername("naoexiste@teste.com"));
        verify(userRepository).findAll();
    }

    @Test
    void deveLancarExcecaoQuandoListaDeUsuariosVazia() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class,
                () -> authorizationService.loadUserByUsername("qualquer@teste.com"));
        verify(userRepository).findAll();
    }
}
