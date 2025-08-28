package com.marketplace.ifba.repository;

import com.marketplace.ifba.model.GrupoPesquisa;
import com.marketplace.ifba.model.Instituicao;
import com.marketplace.ifba.model.Organizacao;
import com.marketplace.ifba.model.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, UUID> {
    Optional<Projeto> findByNome(String nome);

    // adcionado metodo para buscar projetos por grupo de pesquisa
    List<Projeto> findByGrupoPesquisa(GrupoPesquisa grupoPesquisa);

    // adcionado metodo para buscar projetos por instituicao
    List<Projeto> findByInstituicao(Instituicao instituicao);

    // adcionado metodo para buscar projetos por organizacao
    List<Projeto> findByOrganizacao(Organizacao organizacao);

}
