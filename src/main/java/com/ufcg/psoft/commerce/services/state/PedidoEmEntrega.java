package com.ufcg.psoft.commerce.services.state;

import com.ufcg.psoft.commerce.exception.CommerceException;
import com.ufcg.psoft.commerce.model.Enums.StatusPedido;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.services.Impl.PedidoServiceImpl;

public class PedidoEmEntrega implements StateStatusDoPedido{

    @Override
    public void alterarStatus(Pedido pedido, PedidoServiceImpl pedidoService) {
        if (pedido.getStatusPedido() != StatusPedido.PEDIDO_EM_ENTREGA) throw new CommerceException("Esse pedido não está em entrega.");

        pedido.setStatusPedido(StatusPedido.PEDIDO_ENTREGUE);
        pedidoService.setEstadoDoPedido(new PedidoEntregue());

    }
}
