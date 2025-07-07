/*package com.marketplace.ifba.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "grupo_pesquisa")
public class GrupoPesquisa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID idGrupoPesquisa;
    private String titulo;
    private Instituicao instituicao;
    private Date dataRegistro;
    private ArrayList<Tag> tags;
    private String descricao;
    private Number trabalhos;
    private Double classificacao;
    private ArrayList<Usuario> usuarios;

}*/
