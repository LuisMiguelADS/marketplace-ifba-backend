package com.marketplace.ifba.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "tb_chats")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_chat", updatable = false, nullable = false)
    private UUID idChat;

    @Column(name = "date_created_chat", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "date_finished_chat")
    private LocalDateTime dataEncerrado;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChatUsuario> chatUsuarios;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Mensagem> mensagens;

    @OneToOne(mappedBy = "chat", fetch = FetchType.LAZY)
    private Projeto projeto;
}
