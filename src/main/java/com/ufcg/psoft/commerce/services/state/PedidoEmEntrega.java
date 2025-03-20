package com.ufcg.psoft.commerce.services.state;

import com.ufcg.psoft.commerce.exception.CommerceException;
import com.ufcg.psoft.commerce.model.Enums.StatusPedido;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.services.Impl.PedidoServiceImpl;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PEDIDO_EM_ENTREGA")
public class PedidoEmEntrega extends StateStatusDoPedido{

    @Override
    public void alterarStatus(Pedido pedido) {
        if (!pedido.getStatusPedido().toString().equals("PEDIDO_EM_ENTREGA")) throw new CommerceException("Esse pedido não está em entrega.");

        this.getPedido().setStatusPedido(new PedidoEntregue());
        this.getPedido().getStatusPedido().setPedido(this.getPedido());
    }

    @Override
    public String toString() {
        return "PEDIDO_EM_ENTREGA";
    }

}
