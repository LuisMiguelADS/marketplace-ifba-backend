package com.marketplace.ifba.mapper;

import com.marketplace.ifba.dto.UserRequest;
import com.marketplace.ifba.dto.UserResponse;
import com.marketplace.ifba.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRequest request) {
        if (request == null) {
            return null;
        }

        User user = new User();
        user.setNomeCompleto(request.nomeCompleto());
        user.setRole(request.role());
        user.setEmail(request.email());
        user.setTelefone(request.telefone());
        user.setPassword(request.password());
        user.setCpf(request.cpf());
        user.setDataNascimento(request.dataNascimento());
        user.setBiografia(request.biografia());
//        user.setFotoPerfilURL(request.fotoPerfilURL());
//        user.setEndereco(request.endereco());
//        user.setInstituicao(request.instituicao());
//        user.setOrganizacao(request.organizacao());
        return user;
    }

    public UserResponse toDTO(User user) {
        if (user == null) {
            return null;
        }

        return new UserResponse(
                user.getIdUsuario(),
                user.getNomeCompleto(),
                user.getRole(),
                user.getEmail(),
                user.getTelefone(),
                user.getDataRegistro(),
                user.getDataNascimento(),
                user.getBiografia()
        );
    }

    public User updateEntityFromRequest(UserRequest request, User user) {
        if (request == null) {
            throw new IllegalArgumentException("UserRequest não pode ser nulo para atualização.");
        }
        if (user == null) {
            throw new IllegalArgumentException("Usuário a ser atualizado não pode ser nulo.");
        }
        if (request.nomeCompleto() != null) {
            user.setNomeCompleto(request.nomeCompleto());
        }
        if (request.telefone() != null) {
            user.setTelefone(request.telefone());
        }
        if (request.password() != null || request.password() != user.getPassword()) {
            user.setPassword(request.password());
        }
        if (request.dataNascimento() != null) {
            user.setDataNascimento(request.dataNascimento());
        }
        if (request.biografia() != null) {
            user.setBiografia(request.biografia());
        }
//        if (request.fotoPerfilURL() != null) {
//            user.setFotoPerfilURL(request.fotoPerfilURL());
//        }
//        if (request.endereco() != null) {
//            user.setEndereco(request.endereco());
//        }
//        if (request.instituicao() != null) {
//            user.setInstituicao(request.instituicao());
//        }
//        if (request.organizacao() != null) {
//            user.setOrganizacao(request.organizacao());
//        }

        return user;
    }
}
