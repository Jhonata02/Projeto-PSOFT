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
public class Fornecedor {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("nome")
    @Column(nullable = false)
    private String nome;

    @JsonProperty("cnpj")
    @Column(nullable = false)
    private String cnpj;

    @JsonIgnore
    @Column(nullable = false)
    private String codigo;

    @JsonProperty("entregadores")
    @ManyToMany()
    private List<Entregador> entregadores;

    @JsonProperty("cafes")
    @JsonIgnoreProperties("fornecedor")
    @OneToMany(cascade = CascadeType.ALL)
    private List<Cafe> cafes;
}
