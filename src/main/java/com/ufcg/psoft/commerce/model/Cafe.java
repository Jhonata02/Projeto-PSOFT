package com.ufcg.psoft.commerce.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cafe {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("nome")
    @Column(nullable = false)
    private String nome;

    @JsonProperty("origem")
    @Column(nullable = false)
    private String origem;

    @JsonProperty("tipo")
    @Column(nullable = false)
    private String tipo;

    @JsonProperty("PerfilSensorial")
    @Column(nullable = false)
    private String perfilSensorial;

    @JsonProperty("preco")
    @Column(nullable = false)
    private double preco;

    @JsonProperty("disponibilidade")
    @Column(nullable = false)
    private boolean isDisponivel;

    @JsonProperty("isPremium")
    @Column(nullable = false)
    private boolean isPremium;

    @JsonProperty("tamanhoEmbalagem")
    @Column(nullable = false)
    private String tamanhoEmbalagem;

    @JsonProperty("fornecedor")
    @ManyToOne()
    private Fornecedor fornecedor;
}
