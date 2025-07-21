package com.marketplace.ifba.model;

import com.marketplace.ifba.model.enums.StatusGrupoPesquisa;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Data
@Entity
@Table(name = "tb_research_groups")
public class GrupoPesquisa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_research_group", updatable = false, nullable = false)
    private UUID idGrupoPesquisa;

    @Size(max = 50)
    @Column(name = "name", unique = true, nullable = false)
    private String nome;

    @Size(max = 2000)
    @Column(name = "description", length = 2000)
    private String descricao;

    @PositiveOrZero
    @Column(name = "works_development")
    private Integer trabalhos;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "5.0")
    @Column(name = "stars_rating")
    private Double classificacao;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    private StatusGrupoPesquisa status;

    @Column(name = "date_platform_registration", nullable = false, updatable = false)
    private LocalDateTime dataRegistro;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tb_research_group_users",
            joinColumns = @JoinColumn(name = "research_group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private ArrayList<User> usuarios;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tb_research_group_tags",
            joinColumns = @JoinColumn(name = "research_group_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private ArrayList<Tag> tags;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id")
    private Instituicao instituicao;
}
