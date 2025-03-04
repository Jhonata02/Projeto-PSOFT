package com.ufcg.psoft.commerce.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.model.Cafe;
import com.ufcg.psoft.commerce.model.ItemPedido;
import com.ufcg.psoft.commerce.model.Pedido;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponseDTO {

    @Id
    @JsonProperty("id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("cafe")
    private Cafe cafe;

    @JsonProperty("pedido")
    @JsonIgnoreProperties({"itens"})
    private Pedido pedido;

    @JsonProperty("valor")
    private double valorDoItem;

    public ItemResponseDTO(ItemPedido itemPedido) {
        this.id = itemPedido.getId();
        this.cafe = itemPedido.getCafe();
        this.pedido = itemPedido.getPedido();
        this.valorDoItem = itemPedido.getValorDoItem();
    }
}
