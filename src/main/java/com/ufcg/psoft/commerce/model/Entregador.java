package com.ufcg.psoft.commerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Entregador {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("nome")
    @Column(nullable = false)
    private String nome;

    @JsonProperty("placa")
    @Column(nullable = false)
    private String placa;

    @JsonProperty("tipoVeiculo")
    @Column(nullable = false)
    private String tipoVeiculo;

    @JsonProperty("corDoVeiculo")
    @Column(nullable = false)
    private String corDoVeiculo;

    @JsonIgnore
    @Column(nullable = false)
    private String codigo;

    @JsonProperty("pedidos")
    @JsonIgnoreProperties("entregador")
    @OneToMany(mappedBy = "entregador")
    private List<Pedido> pedidos;
}
