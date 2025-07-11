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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_project", updatable = false, nullable = false)
    private UUID idProjeto;

    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String nome;

    @Size(max = 20)
    @Column(name = "status", nullable = false)
    private StatusProjeto status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organizacao organizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id")
    private Instituicao instituicao;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "demand_id")
    private Demanda demanda;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_solution_id")
    private OfertaSolucao solucaoOferta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "research_group_id")
    private GrupoPesquisa grupoPesquisa;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "chat_id")
    private Chat chat;
}
