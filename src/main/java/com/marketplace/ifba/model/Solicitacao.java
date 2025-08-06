package com.marketplace.ifba.model;

import com.marketplace.ifba.model.enums.StatusSolicitacao;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "tb_request")
public class Solicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_solicitacao", updatable = false, nullable = false)
    private UUID idSolicitacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_applicant_id", updatable = false, nullable = false)
    private User userApplicant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_requested_id", updatable = false, nullable = false)
    private Organizacao organizacaoRequested;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    private StatusSolicitacao status;
}
