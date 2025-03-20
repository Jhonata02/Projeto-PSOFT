package com.ufcg.psoft.commerce.services.state;

import com.ufcg.psoft.commerce.exception.CommerceException;
import com.ufcg.psoft.commerce.model.Enums.StatusPedido;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.services.Impl.PedidoServiceImpl;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PEDIDO_ENTREGUE")
public class PedidoEntregue extends StateStatusDoPedido{

    @Override
    public void alterarStatus(Pedido pedido) {
        // TODO Do nothing
    }

    @Override
    public String toString(){
        return "PEDIDO_ENTREGUE";
    }
}
