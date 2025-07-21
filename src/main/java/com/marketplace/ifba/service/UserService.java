package com.marketplace.ifba.service;

import com.marketplace.ifba.dto.UserRequest;
import com.marketplace.ifba.dto.UserResponse;
import com.marketplace.ifba.exception.DadoConflitoException;
import com.marketplace.ifba.exception.DadoNaoEncontradoException;
import com.marketplace.ifba.mapper.UserMapper;
import com.marketplace.ifba.model.User;
import com.marketplace.ifba.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    // ---------- LEITURA

    // BUSCA USUÁRIO A PARTIR DO SEU TOKEN DE ACESSO
    public UserResponse buscarUsuarioPorToken(String token) {
        String emailLogin = tokenService.validateToken(token);
        User user = userRepository.findByEmail(emailLogin);

        if (user == null) {
            throw new DadoNaoEncontradoException("Usuário não encontrado para o token fornecido.");
        }

        return userMapper.toDTO(user);
    }

    // BUSCA USUÁRIO PELO SEU ID
    public UserResponse buscarUsuarioPorID(UUID idUsuario) {
        return userMapper.toDTO(userRepository.findById(idUsuario)
                .orElseThrow(() -> new DadoNaoEncontradoException("Usuário não encontrada com o ID: " + idUsuario)));
    }

    // LISTA TODOS OS USUÁRIOS DO SISTEMA
    public List<UserResponse> buscarTodosUsuarios() {
        return userRepository.findAll().stream().map(userMapper::toDTO).toList();
    }

    // ---------- ESCRITA

    // REGISTRO DO USUÁRIO
    public UserResponse registrarUsuario(UserRequest request) {
        if (userRepository.findByEmail(request.email()) != null) {
            throw new DadoConflitoException("Email já registrado.");
        }

        String encryptedPassword = passwordEncoder.encode(request.password());

        User newUser = userMapper.toEntity(request);
        newUser.setPassword(encryptedPassword);
        newUser.setDataRegistro(LocalDateTime.now());

        User savedUser = userRepository.save(newUser);

        return userMapper.toDTO(savedUser);
    }

    // ATUALIZA USUÁRIO
    public UserResponse atualizarUsuario(UUID idUsuario, UserRequest request) {
        User user = userRepository.findById(idUsuario).orElseThrow(() -> new DadoNaoEncontradoException("Usuário não encontrada com o ID: " + idUsuario));
        User userAtualizado = userMapper.updateEntityFromRequest(request, user);
        String encryptedPassword = passwordEncoder.encode(userAtualizado.getPassword());
        userAtualizado.setPassword(encryptedPassword);

        return userMapper.toDTO(userRepository.save(userAtualizado));
    }

    // REMOVE USUÁRIO PELO SEU ID
    public void removerUsuario(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuário não encontrado! ID: " + id);
        }
        userRepository.deleteById(id);
    }
}
