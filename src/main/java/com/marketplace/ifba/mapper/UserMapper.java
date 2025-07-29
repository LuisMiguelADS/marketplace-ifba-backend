package com.marketplace.ifba.mapper;

import com.marketplace.ifba.dto.InstituicaoResponse;
import com.marketplace.ifba.dto.OrganizacaoResponse;
import com.marketplace.ifba.dto.UserRequest;
import com.marketplace.ifba.dto.UserResponse;
import com.marketplace.ifba.model.Conexao;
import com.marketplace.ifba.model.Organizacao;
import com.marketplace.ifba.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        return user;
    }

    public UserResponse toDTO(User user) {
        if (user == null) {
            return null;
        }

        List<LocalDateTime> conexoes = new ArrayList<>();

        for (Conexao conexao : user.getConexoes()) {
            conexoes.add(conexao.getDataConexao());
        }

        return new UserResponse(
                user.getIdUsuario(),
                user.getNomeCompleto(),
                user.getRole(),
                user.getEmail(),
                user.getTelefone(),
                user.getDataRegistro(),
                user.getDataNascimento(),
                user.getBiografia(),
                conexoes,
                (user.getOrganizacao() != null) ? user.getOrganizacao().getIdOrganizacao() : null,
                (user.getInstituicao() != null) ? user.getInstituicao().getIdInstituicao() : null
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

        return user;
    }
}
