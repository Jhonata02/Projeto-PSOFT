package com.ufcg.psoft.commerce.services.state;

import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.services.Impl.PedidoServiceImpl;

public interface StateStatusDoPedido {

    void alterarStatus(Pedido pedido, PedidoServiceImpl pedidoService);
}
