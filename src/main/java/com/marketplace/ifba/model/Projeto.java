package com.marketplace.ifba.model;

import com.marketplace.ifba.model.enums.StatusProjeto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

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

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    private StatusProjeto status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", updatable = false)
    private Organizacao organizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id", updatable = false)
    private Instituicao instituicao;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "demand_id", updatable = false)
    private Demanda demanda;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_solution_id", updatable = false)
    private OfertaSolucao solucaoOferta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "research_group_id", updatable = false)
    private GrupoPesquisa grupoPesquisa;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "chat_id", updatable = false)
    private Chat chat;
}
