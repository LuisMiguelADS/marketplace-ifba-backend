package com.marketplace.ifba.model;

import com.marketplace.ifba.model.enums.StatusProposta;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "tb_proposals")
public class Proposta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_proposal", updatable = false, nullable = false)
    private UUID idProposta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "research_group_id")
    private GrupoPesquisa grupoPesquisa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id")
    private Instituicao instituicao;

    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String nome;

    @Size(max = 1000)
    @Column(name = "solution", length = 1000)
    private String solucao;

    @Size(max = 2000)
    @Column(name = "description", length = 2000)
    private String descricao;

    @Size(max = 500)
    @Column(name = "resume", length = 500)
    private String resumo;

    @Column(name = "budget")
    private Double orcamento;

    @Size(max = 500)
    @Column(name = "restrictions", length = 500)
    private String restricoes;

    @Size(max = 500)
    @Column(name = "necessary_resources", length = 500)
    private String recursosNecessarios;

    @Size(max = 20)
    @Column(name = "status", nullable = false)
    private StatusProposta status;

    @FutureOrPresent()
    @Column(name = "date_term_proposal")
    private LocalDateTime dataPrazoProposta;

    @Column(name = "date_registration", nullable = false, updatable = false)
    private LocalDateTime dataRegistro;
}
