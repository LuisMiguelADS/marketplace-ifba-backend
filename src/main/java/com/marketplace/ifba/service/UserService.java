package com.marketplace.ifba.service;

import com.marketplace.ifba.dto.UserRequest;
import com.marketplace.ifba.dto.UserResponse;
import com.marketplace.ifba.exception.DadoNaoEncontradoException;
import com.marketplace.ifba.mapper.UserMapper;
import com.marketplace.ifba.model.User;
import com.marketplace.ifba.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenService tokenService;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponse salvarUsuario(UserRequest request) {
        if (userRepository.findByEmail(request.email()) != null) {
            throw new IllegalArgumentException("Email já registrado.");
        }

        String encryptedPassword = passwordEncoder.encode(request.password());

        User newUser = userMapper.toEntity(request);
        newUser.setPassword(encryptedPassword);
        newUser.setDataRegistro(LocalDateTime.now());

        User savedUser = userRepository.save(newUser);

        return userMapper.toDTO(savedUser);
    }

    public List<UserResponse> listarUsario() {
        return userRepository.findAll().stream().map(userMapper::toDTO).toList();
    }

    public UserResponse buscarUsuarioPorToken(String token) {
        String emailLogin;
        emailLogin = tokenService.validateToken(token);
        UserDetails userDetails = userRepository.findByEmail(emailLogin);

        if (userDetails == null) {
            throw new DadoNaoEncontradoException("Usuário não encontrado para o token fornecido.");
        }

        User user = (User) userDetails;
        UserResponse userResponse = userMapper.toDTO(user);
        return userResponse;
    }

    public UserResponse buscarPorID(UUID id) {
        return userMapper.toDTO(userRepository.findById(id).orElseThrow());
    }

    public void removerUsuario(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuário não encontrado! ID: " + id);
        }
        userRepository.deleteById(id);
    }

    public UserResponse atualizar(UUID id, UserRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado!"));
        User userAtualizado = userMapper.updateEntityFromRequest(request, user);
        String encryptedPassword = passwordEncoder.encode(userAtualizado.getPassword());
        userAtualizado.setPassword(encryptedPassword);

        return userMapper.toDTO(userRepository.save(userAtualizado));
    }
}
