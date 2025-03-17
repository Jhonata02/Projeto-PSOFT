package com.ufcg.psoft.commerce.services.state;

import com.ufcg.psoft.commerce.exception.CommerceException;
import com.ufcg.psoft.commerce.model.Enums.StatusPedido;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.services.Impl.PedidoServiceImpl;

public class PedidoEntregue implements StateStatusDoPedido{

    @Override
    public void alterarStatus(Pedido pedido, PedidoServiceImpl pedidoService) {

    }
}
