package com.ufcg.psoft.commerce.services.state;

import com.ufcg.psoft.commerce.model.Enums.StatusPedido;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.services.Impl.PedidoServiceImpl;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PREPARACAO")
public class Preparacao extends StateStatusDoPedido{

    @Override
    public void alterarStatus(Pedido pedido) {
        if(!pedido.getStatusPedido().toString().equals("PREPARACAO")) {
            throw new RuntimeException("O Status do pedido não é PREPARAÇÃO");
        }
        this.getPedido().setStatusPedido(new PedidoPronto());
        this.getPedido().getStatusPedido().setPedido(this.getPedido());
    }

    @Override
    public String toString() {
        return "PREPARACAO";
    }
}
