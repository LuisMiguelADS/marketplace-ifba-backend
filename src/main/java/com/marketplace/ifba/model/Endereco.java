package com.marketplace.ifba.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "endereco")
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_address", updatable = false, nullable = false)
    private UUID idEndereco;

    @Size(min = 8, max = 9)
    @Column(name = "cep", length = 9, nullable = false)
    private String CEP;

    @Positive()
    @Column(name = "house_number", nullable = false)
    private Number numero;

    @Size(max = 50)
    @Column(name = "road", nullable = false)
    private String rua;

    @Size(max = 50)
    @Column(name = "neighborhood", nullable = false)
    private String bairro;

    @Size(max = 50)
    @Column(name = "city", nullable = false)
    private String cidade;

    @Size(min = 2, max = 2)
    @Column(name = "state", length = 2, nullable = false)
    private String estado;

    @Size(max = 50)
    @Column(name = "country", nullable = false)
    private String pais;

    @Size(max = 100)
    @Column(name = "complement")
    private String complemento;
}
