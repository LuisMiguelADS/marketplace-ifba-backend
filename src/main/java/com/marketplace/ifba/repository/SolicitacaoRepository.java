package com.marketplace.ifba.repository;

import com.marketplace.ifba.model.Solicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, UUID> {
}