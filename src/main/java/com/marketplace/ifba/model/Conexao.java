package com.marketplace.ifba.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "tb_connections")
public class Conexao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_connection", updatable = false, nullable = false)
    private UUID idConexao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User usuarioConectado;

    @Column(name = "date_connected", nullable = false, updatable = false)
    private LocalDateTime dataConexao;
}
