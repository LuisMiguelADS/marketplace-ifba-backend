package com.marketplace.ifba.repository;

import com.marketplace.ifba.model.Organizacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrganizacaoRepository extends JpaRepository<Organizacao, UUID> {
    Optional<Organizacao> findByNome(String nome);
    Optional<Organizacao> findByCnpj(String cnpj);
}
