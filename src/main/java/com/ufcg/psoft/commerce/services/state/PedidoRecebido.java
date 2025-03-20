package com.ufcg.psoft.commerce.services.state;

import com.ufcg.psoft.commerce.model.Enums.StatusPedido;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.services.Impl.PedidoServiceImpl;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PEDIDO_RECEBIDO")
public class PedidoRecebido extends StateStatusDoPedido{

    @Override
    public void alterarStatus(Pedido pedido) {
        super.getPedido().setStatusPedido(new Preparacao());
        super.getPedido().getStatusPedido().setPedido(super.getPedido());
    }

    @Override
    public String toString() {
        return "PEDIDO_RECEBIDO";
    }
}
