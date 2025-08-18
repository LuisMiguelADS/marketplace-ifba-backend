package com.marketplace.ifba.mapper;

import com.marketplace.ifba.dto.*;
import com.marketplace.ifba.model.Conexao;
import com.marketplace.ifba.model.Instituicao;
import com.marketplace.ifba.model.Organizacao;
import com.marketplace.ifba.model.User;
import com.marketplace.ifba.service.InstituicaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserMapper {

    private final InstituicaoService instituicaoService;

    public UserMapper(InstituicaoService instituicaoService) {
        this.instituicaoService = instituicaoService;
    }

    public User toEntity(UserRequest request) {
        if (request == null) {
            return null;
        }

        Instituicao instituicao = null;
        if (request.idInstituicao() != null) {
            instituicao = instituicaoService.buscarInstituicaoPorId(request.idInstituicao());
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
        user.setInstituicao(instituicao);
        return user;
    }

    public UserResponse toDTO(User user) {
        if (user == null) {
            return null;
        }

        List<LocalDateTime> conexoes = new ArrayList<>();

        if (user.getConexoes() != null) {
            for (Conexao conexao : user.getConexoes()) {
                conexoes.add(conexao.getDataConexao());
            }
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
                (user.getInstituicao() != null) ? user.getInstituicao().getIdInstituicao() : null,
                (user.getGrupoPesquisa() != null) ? user.getGrupoPesquisa().getIdGrupoPesquisa() : null
        );
    }

   public UserInfosMinResponse toDTOInfosMin(User user) {
        if (user == null) {
            return null;
        }

        return new UserInfosMinResponse(
                user.getIdUsuario(),
                user.getNomeCompleto(),
                user.getEmail(),
                user.getTelefone()
        );
   }
}
