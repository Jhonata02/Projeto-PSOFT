package com.ufcg.psoft.commerce.model;

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
public class Pedido {

    @Id
    @JsonProperty("id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("itens")
    @JsonIgnoreProperties("pedido")
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itens;

    @JsonProperty("enderecoDeEntrega")
    private String enderecoDeEntrega;

    @JsonProperty("cliente")
    @JsonIgnoreProperties("pedidos")
    @ManyToOne()
    private Cliente cliente;

    @JsonProperty("valorPedido")
    private double valorPedido;

    @Enumerated(EnumType.STRING)
    @JsonProperty("statusPedido")
    private StatusPedido statusPedido;

    @Enumerated(EnumType.STRING)
    @JsonProperty("metodoPagamento")
    private MetodoPagamento metodoPagamento;

    @JsonProperty("entregador")
    @ManyToOne()
    private Entregador entregador;

    @JsonProperty("fornecedor")
    @ManyToOne()
    private Fornecedor fornecedor;

}
