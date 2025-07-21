package com.marketplace.ifba.model;

import com.marketplace.ifba.model.enums.StatusOfertaSolucao;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "tb_offer_solutions")
public class OfertaSolucao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_offer_solution", updatable = false, nullable = false)
    private UUID idSolucao;

    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String nome;

    @Size(max = 2000)
    @Column(name = "description", length = 2000)
    private String descricao;

    @Min(value = 0)
    @Column(name = "days_term")
    private Integer prazo;

    @Size(max = 500)
    @Column(name = "resume", length = 500)
    private String resumo;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    private StatusOfertaSolucao status;

    @Column(name = "aprovation")
    private Boolean aprovado;

    @Size(max = 50)
    @Column(name = "type_solution", nullable = false)
    private String tipoSolucao;

    @Size(max = 500)
    @Column(name = "restrictions", length = 500)
    private String restricao;

    @Column(name = "estimated_price")
    private Double preco;

    @Size(max = 500)
    @Column(name = "necessary_resources", length = 500)
    private String recursoNecessario;

    @Column(name = "date_aprovation", updatable = false)
    private LocalDateTime dataAprovacao;

    @Column(name = "date_registration", nullable = false, updatable = false)
    private LocalDateTime dataRegistro;
}
