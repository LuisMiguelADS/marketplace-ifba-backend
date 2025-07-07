package com.marketplace.ifba.service;

import com.marketplace.ifba.dto.UsuarioRequest;
import com.marketplace.ifba.dto.UsuarioResponse;
import com.marketplace.ifba.mapper.UsuarioMapper;
import com.marketplace.ifba.model.Usuario;
import com.marketplace.ifba.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    public UsuarioResponse salvarUsuario(UsuarioRequest request) {
        Usuario usuario = usuarioMapper.toEntity(request);
        usuario.setDataRegistro(LocalDate.now());
        usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(usuario);
    }

    public List<UsuarioResponse> listarUsario() {
        return usuarioRepository.findAll().stream().map(usuarioMapper::toDTO).toList();
    }

    public UsuarioResponse buscarPorID(UUID id) {
        return usuarioMapper.toDTO(usuarioRepository.findById(id).orElseThrow());
    }

    public void removerUsuario(UUID id) {
        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuário não encontrado! ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    public UsuarioResponse atualizar(UUID id, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));
        usuario.setNomeCompleto(request.getNomeCompleto());
        usuario.setEmail(request.getEmail());
        usuario.setTelefone(request.getTelefone());
        usuario.setSenha(request.getSenha());
        usuario.setBiografia(request.getBiografia());
        usuario.setEndereco(request.getEndereco());
        return usuarioMapper.toDTO(usuarioRepository.save(usuario));
    }
}
