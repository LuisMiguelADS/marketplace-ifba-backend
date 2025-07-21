package com.marketplace.ifba.repository;

import com.marketplace.ifba.model.OfertaSolucao;
import com.marketplace.ifba.model.enums.StatusOfertaSolucao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OfertaSolucaoRepository extends JpaRepository<OfertaSolucao, UUID> {
    List<OfertaSolucao> findByNome(String nome);
    List<OfertaSolucao> findByStatus(StatusOfertaSolucao status);
    List<OfertaSolucao> findByAprovado(Boolean aprovado);
}
