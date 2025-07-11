/*package com.marketplace.ifba.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@Entity
@Data
@Table(name = "chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idChat;
    private Date dataCriacao;
    private Date dataEncerrado;
    private HashMap<LocalDateTime, User> usuarioView;
    private Mensagem mensagem;

}*/
