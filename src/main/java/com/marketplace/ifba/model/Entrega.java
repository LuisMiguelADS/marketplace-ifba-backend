package com.marketplace.ifba.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

import com.marketplace.ifba.model.enums.StatusEntrega;

@Data
@Entity
@Table(name = "tb_entregas")
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_entrega", updatable = false, nullable = false)
    private UUID idEntrega;

    @Size(max = 255)
    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Size(max = 2000)
    @Column(name = "descricao", length = 2000)
    private String descricao;

    @Column(name = "prazo_desejado")
    private LocalDate prazoDesejado;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDate dataCriacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacao_solicitante_id")
    private Organizacao organizacaoSolicitante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_pesquisa_solicitante_id")
    private GrupoPesquisa grupoPesquisaSolicitante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacao_solicitada_id")
    private Organizacao organizacaoSolicitada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_pesquisa_solicitado_id")
    private GrupoPesquisa grupoPesquisaSolicitado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projeto_id", nullable = false)
    private Projeto projeto;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    private StatusEntrega status;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDate.now();
    }
}