package com.marketplace.ifba.service;

import com.marketplace.ifba.dto.UserRequest;
import com.marketplace.ifba.dto.UserResponse;
import com.marketplace.ifba.mapper.UserMapper;
import com.marketplace.ifba.model.User;
import com.marketplace.ifba.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponse salvarUsuario(UserRequest request) {
        User user = userMapper.toEntity(request);
        user.setDataRegistro(LocalDateTime.now());
        userRepository.save(user);
        return userMapper.toDTO(user);
    }

    public List<UserResponse> listarUsario() {
        return userRepository.findAll().stream().map(userMapper::toDTO).toList();
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
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));
        user.setNomeCompleto(request.getNomeCompleto());
        user.setRole(request.getRole());
        user.setEmail(request.getEmail());
        user.setTelefone(request.getTelefone());
        user.setPassword(request.getPassword());
        user.setBiografia(request.getBiografia());
        user.setEndereco(request.getEndereco());
        return userMapper.toDTO(userRepository.save(user));
    }
}
