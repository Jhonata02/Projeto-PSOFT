package com.ufcg.psoft.commerce.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido {

    @Id
    @JsonProperty("id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("cafe")
    @ManyToOne()
    private Cafe cafe;

    @JsonProperty("pedido")
    @JsonIgnoreProperties("itens")
    @ManyToOne()
    private Pedido pedido;

    @JsonProperty("valor")
    private double valorDoItem;
}
