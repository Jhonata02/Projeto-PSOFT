package com.ufcg.psoft.commerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.event.PedidoEntregueEvent;
import com.ufcg.psoft.commerce.listener.PedidoEntregueListener;
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
public class Fornecedor implements PedidoEntregueListener{

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
    @OneToMany(mappedBy = "fornecedor", cascade = CascadeType.ALL)
    private List<Cafe> cafes;

    @JsonProperty("pedidos")
    @OneToMany(mappedBy = "fornecedor")
    private List<Pedido> pedidos;

    @Override
    public void notificaPedidoEntregue(PedidoEntregueEvent pedidoEntregueEvent) {
        Fornecedor fornecedor = pedidoEntregueEvent.getPedido().getFornecedor();
        System.out.println(
                "\nFornecedor: " + fornecedor.getNome()
                        + " Id: " + fornecedor.getId()
                        + "\nO pedido de numero: " + pedidoEntregueEvent.getPedido().getId()
                        + ", foi entregue ao cliente!"
        );
    }
}
