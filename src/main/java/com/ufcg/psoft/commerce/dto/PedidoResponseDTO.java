package com.ufcg.psoft.commerce.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.model.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponseDTO {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("itens")
    private List<ItemPedido> itens;

    @JsonProperty("enderecoDeEntrega")
    private String enderecoDeEntrega;

    @JsonProperty("valorPedido")
    private double valorPedido;

    @JsonProperty("statusPedido")
    private StatusPedido statusPedido;

    @JsonProperty("metodoPagamento")
    private MetodoPagamento metodoPagamento;

    @JsonProperty("cliente")
    @JsonIgnoreProperties({"pedidos"})
    private Cliente cliente;

    public PedidoResponseDTO(Pedido pedido) {
        this.id = pedido.getId();
        this.itens = pedido.getItens();
        this.enderecoDeEntrega = pedido.getEnderecoDeEntrega();
        this.valorPedido = pedido.getValorPedido();
        this.statusPedido = pedido.getStatusPedido();
        this.metodoPagamento = pedido.getMetodoPagamento();
        this.cliente = pedido.getCliente();
    }


}
