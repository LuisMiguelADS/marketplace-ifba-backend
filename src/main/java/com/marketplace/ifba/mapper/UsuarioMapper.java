package com.marketplace.ifba.mapper;

import com.marketplace.ifba.dto.UsuarioRequest;
import com.marketplace.ifba.dto.UsuarioResponse;
import com.marketplace.ifba.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    public Usuario toEntity(UsuarioRequest request);

    public UsuarioResponse toDTO(Usuario usuario);
}
