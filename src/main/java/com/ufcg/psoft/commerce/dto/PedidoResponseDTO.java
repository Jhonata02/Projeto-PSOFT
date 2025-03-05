package com.ufcg.psoft.commerce.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.model.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponseDTO {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    //@JsonProperty("itens")
    //private List<ItemPedido> itens;

    @JsonProperty("cafes")
    private List<Long> cafesId;

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
    private Long clienteId;

    @JsonProperty("entregador")
    @JsonIgnoreProperties("pedido")
    private EntregadorResponseDTO entregador;

    public PedidoResponseDTO(Pedido pedido) {
        this.id = pedido.getId();
        this.cafesId = pedido.getItens().stream()
                .map(itemPedido -> itemPedido.getCafe()).map(cafe -> cafe.getId())
                .collect(Collectors.toList());
        //this.itens = pedido.getItens();
        this.enderecoDeEntrega = pedido.getEnderecoDeEntrega();
        this.valorPedido = Math.round(pedido.getValorPedido()*100.00)/100.00;
        this.statusPedido = pedido.getStatusPedido();
        this.metodoPagamento = pedido.getMetodoPagamento();
        this.clienteId = pedido.getCliente().getId();
        this.entregador = pedido.getEntregador() != null ? new EntregadorResponseDTO(pedido.getEntregador()) : null;
    }


}
