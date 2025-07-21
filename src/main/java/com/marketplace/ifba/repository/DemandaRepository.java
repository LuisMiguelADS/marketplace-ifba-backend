package com.marketplace.ifba.repository;

import com.marketplace.ifba.model.Demanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DemandaRepository extends JpaRepository<Demanda, UUID> {
    Optional<Demanda> findByNome(String nome);
}
