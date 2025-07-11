package com.marketplace.ifba.model;

import com.marketplace.ifba.model.enums.StatusDemanda;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "tb_demands")
public class Demanda {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_demand", updatable = false, nullable = false)
    private UUID idDemanda;

    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String nome;

    @Email()
    @Column(name = "email_responsible", nullable = false)
    private String emailResponsavel;

    @Column(name = "budget", nullable = false)
    private Double orcamento;

    @Size(max = 2000)
    @Column(name = "description", length = 2000, nullable = false)
    private String descricao;

    @Size(max = 500)
    @Column(name = "resume", length = 500, nullable = false)
    private String resumo;

    @Size(max = 1000)
    @Column(name = "criteria", length = 1000, nullable = false)
    private String criterio;

    @Size(max = 20)
    @Column(name = "status", nullable = false)
    private StatusDemanda status;

    @Column(name = "organization_aprovation", nullable = false)
    private Boolean aprovacaoDemandante;

    @Min(value = 0)
    @Column(name = "views_count", nullable = false)
    private Integer vizualizacoes;

    @FutureOrPresent()
    @Column(name = "date_term_demand", nullable = false)
    private LocalDateTime dataPrazoFinal;

    @Column(name = "date_approved_demand")
    private LocalDateTime dataAprovado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_user_id", nullable = false)
    private User usuarioCriador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organizacao organizacao;
}
