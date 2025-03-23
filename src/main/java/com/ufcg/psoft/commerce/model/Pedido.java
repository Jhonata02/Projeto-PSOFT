package com.ufcg.psoft.commerce.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.model.Enums.MetodoPagamento;
import com.ufcg.psoft.commerce.model.Enums.StatusPedido;
import com.ufcg.psoft.commerce.services.pedido.state.StateStatusDoPedido;
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

    @JsonProperty("statusPedido")
    @Enumerated(EnumType.STRING)
    private StatusPedido statusPedido;

    @Transient
    private StateStatusDoPedido statusPedidoState;

    @Enumerated(EnumType.STRING)
    @JsonProperty("metodoPagamento")
    private MetodoPagamento metodoPagamento;

    @JsonProperty("entregador")
    @ManyToOne()
    private Entregador entregador;

    @JsonProperty("fornecedor")
    @ManyToOne()
    private Fornecedor fornecedor;

    @PrePersist
    public void setDefaultValues() {
        if (statusPedido == null) {
            this.statusPedido = StatusPedido.PEDIDO_RECEBIDO;
            this.statusPedidoState = StatusPedido.PEDIDO_RECEBIDO.estadoAtual(this);
        }
    }

    @PostLoad
    public void loadState() {
        // Carrega o estado atual com o Pedido armazenado
        this.statusPedidoState = this.statusPedido.estadoAtual(this);
    }
}
