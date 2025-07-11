package com.marketplace.ifba.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "tb_messages")
public class Mensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_message", updatable = false, nullable = false)
    private UUID idMensagem;

    @Size(max = 1000)
    @Column(name = "message_content", length = 1000, nullable = false)
    private String mensagem;

    @Column(name = "date_message", nullable = false, updatable = false)
    private LocalDateTime dataMensagem;

    @Column(name = "is_active", nullable = false)
    private Boolean ativo;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writing_user_id", nullable = false)
    private User usuarioEscritor;
}
