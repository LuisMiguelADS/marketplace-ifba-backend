package com.marketplace.ifba.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;
/*import java.util.List;*/

@Entity
@Data
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idUsuario;

    @Column(nullable = false)
    private String nomeCompleto;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String telefone;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private String cpf;

    @Column(nullable = false)
    private LocalDate dataRegistro;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    @Column(nullable = false)
    private String biografia;

    @Column(nullable = false)
    private String fotoPerfilURL;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private String instituicao;

    @Column(nullable = false)
    private String organizacao;

    /*@Column(nullable = false)
    private List<Conexao> conexoes;*/
}
