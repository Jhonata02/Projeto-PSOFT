package com.ufcg.psoft.commerce.services.state;

import com.ufcg.psoft.commerce.model.Enums.StatusPedido;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.services.Impl.PedidoServiceImpl;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PEDIDO_PRONTO")
public class PedidoPronto extends StateStatusDoPedido{

    @Override
    public void alterarStatus(Pedido pedido) {
        if(!pedido.getStatusPedido().toString().equals("PEDIDO_PRONTO")) {
            throw new RuntimeException("O Status do pedido não é PEDIDO PRONTO");
        }
        this.getPedido().setStatusPedido(new PedidoEmEntrega());
        this.getPedido().getStatusPedido().setPedido(this.getPedido());
    }

    @Override
    public String toString() {
        return "PEDIDO_PRONTO";
    }
}
