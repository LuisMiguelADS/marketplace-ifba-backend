package com.marketplace.ifba.repository;

import com.marketplace.ifba.model.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PropostaRepository extends JpaRepository<Proposta, UUID> {
    Optional<Proposta> findByNome(String nome);
    List<Proposta> findByGrupoPesquisaIdGrupo(UUID idGrupoPesquisa);
    List<Proposta> findByInstituicaoIdInstituicao(UUID idInstituicao);
}
