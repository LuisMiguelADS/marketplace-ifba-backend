package com.marketplace.ifba.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_tags")
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_tag", updatable = false, nullable = false)
    private UUID idTag;

    @Size(max = 50)
    @Column(name = "name", unique = true, nullable = false)
    private String nomeTag;

}
