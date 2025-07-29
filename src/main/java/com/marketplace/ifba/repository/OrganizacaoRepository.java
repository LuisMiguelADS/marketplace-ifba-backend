package com.marketplace.ifba.repository;

import com.marketplace.ifba.model.Organizacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrganizacaoRepository extends JpaRepository<Organizacao, UUID> {
}
