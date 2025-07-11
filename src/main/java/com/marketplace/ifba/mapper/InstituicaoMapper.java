package com.marketplace.ifba.mapper;

import com.marketplace.ifba.dto.InstituicaoRequest;
import com.marketplace.ifba.dto.InstituicaoResponse;
import com.marketplace.ifba.model.Instituicao;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InstituicaoMapper {
    public Instituicao toEntity(InstituicaoRequest request);

    public InstituicaoResponse toDTO(Instituicao instituicao);
}
