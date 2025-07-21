package com.marketplace.ifba.model;

import com.marketplace.ifba.model.enums.StatusInstituicao;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "instituicao")
public class Instituicao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_institution", updatable = false, nullable = false)
    private UUID idInstituicao;

    @Size(max = 100)
    @Column(name = "name", unique = true, nullable = false)
    private String nome;

    @Size(max = 20)
    @Column(name = "sigla", unique = true)
    private String sigla;

    @Column(name = "cnpj", unique = true, nullable = false)
    private String cnpj;

    @Size(max = 50)
    @Column(name = "institution_type", nullable = false)
    private String tipoInstituicao;

    @Size(max = 30)
    @Column(name = "sector")
    private String setor;

    @Size(max = 15)
    @Column(name = "phone")
    private String telefone;

    @Size(max = 255)
    @Column(name = "site_url")
    private String site;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    private StatusInstituicao status;

    @Size(max = 500)
    @Column(name = "logo_url", length = 500)
    private String logoURL;

    @Size(max = 2000)
    @Column(name = "description", length = 2000)
    private String descricao;

    @Column(name = "date_platform_registration", nullable = false, updatable = false)
    private LocalDateTime dataRegistro;

    @Column(name = "date_platform_aprovation", updatable = false)
    private LocalDateTime dataAprovacao;

    @Column(name = "date_atualization")
    private LocalDateTime dataAtualizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adm_aprovation_id", updatable = false)
    private User admAprovacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_registration_id", nullable = false, updatable = false)
    private User usuarioRegistro;
}
