package com.marketplace.ifba.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserResponse {
    private UUID idUsuario;
    private String nomeCompleto;
    private String email;
    private String telefone;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataRegistro;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;
    private String biografia;
    private String endereco;
}
