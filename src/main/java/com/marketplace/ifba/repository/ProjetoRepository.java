package com.marketplace.ifba.repository;

import com.marketplace.ifba.model.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, UUID> {
    Optional<Projeto> findByNome(String nome);
}
