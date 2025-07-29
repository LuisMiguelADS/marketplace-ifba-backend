package com.marketplace.ifba.model;

import com.marketplace.ifba.model.enums.StatusOrganizacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.br.CNPJ;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "tb_organizations")
public class Organizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_organization", updatable = false, nullable = false)
    private UUID idOrganizacao;

    @Size(max = 30)
    @Column(name = "name", unique = true, nullable = false)
    private String nome;

    @Size(max = 10)
    @Column(name = "acronym", unique = true)
    private String sigla;

    @CNPJ()
    @Column(name = "cnpj", unique = true, nullable = false, updatable = false)
    private String cnpj;

    @Size(max = 30)
    @Column(name = "organization_type", nullable = false)
    private String tipoOrganizacao;

    @Size(max = 50)
    @Column(name = "sector")
    private String setor;

    @Size(max = 15)
    @Column(name = "phone")
    private String telefone;

    @URL()
    @Size(max = 255)
    @Column(name = "site_url")
    private String site;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    private StatusOrganizacao status;

    @Size(max = 2000)
    @Column(name = "description", length = 2000)
    private String descricao;

    @Column(name = "date_platform_registration", nullable = false, updatable = false)
    private LocalDateTime dataRegistro;

    @Column(name = "date_aprovation", updatable = false)
    private LocalDateTime dataAprovacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adm_aprovation_id", updatable = false)
    private User admAprovacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_registration_id", nullable = false, updatable = false)
    private User usuarioRegistro;
}
