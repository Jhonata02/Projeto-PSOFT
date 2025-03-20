package com.ufcg.psoft.commerce.services.state;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.services.Impl.PedidoServiceImpl;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "status_type", discriminatorType = DiscriminatorType.STRING)
@Data
public abstract class StateStatusDoPedido {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @JsonProperty("id")
    private Long id;

    @OneToOne(mappedBy = "statusPedido")
    private Pedido pedido;

    public abstract void alterarStatus(Pedido pedido);
}
