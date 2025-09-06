package com.marketplace.ifba.model;

import com.marketplace.ifba.model.enums.StatusProjeto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "tb_projects")
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_project", updatable = false, nullable = false)
    private UUID idProjeto;

    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String nome;

    @Column(name = "start_date")
    private LocalDate dataInicio;

    @Column(name = "end_date")
    private LocalDate dataFinal;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    private StatusProjeto status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false, updatable = false)
    private Organizacao organizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id", nullable = false, updatable = false)
    private Instituicao instituicao;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "demand_id", nullable = false, updatable = false)
    private Demanda demanda;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_solution_id", updatable = false)
    private OfertaSolucao ofertaSolucao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "research_group_id", nullable = false, updatable = false)
    private GrupoPesquisa grupoPesquisa;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "chat_id", nullable = false, updatable = false)
    private Chat chat;

    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Entrega> entregas;
}
