package com.marketplace.ifba.repository;

import com.marketplace.ifba.model.GrupoPesquisa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GrupoPesquisaRepository extends JpaRepository<GrupoPesquisa, UUID> {
}
