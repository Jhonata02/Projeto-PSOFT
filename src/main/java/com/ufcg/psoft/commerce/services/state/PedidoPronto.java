package com.ufcg.psoft.commerce.services.state;

import com.ufcg.psoft.commerce.model.Enums.StatusPedido;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.services.Impl.PedidoServiceImpl;

public class PedidoPronto implements StateStatusDoPedido{

    @Override
    public void alterarStatus(Pedido pedido, PedidoServiceImpl pedidoService) {
        if(pedido.getStatusPedido() != StatusPedido.PEDIDO_PRONTO) {
            throw new RuntimeException("O Status do pedido não é PEDIDO PRONTO");
        }
        pedido.setStatusPedido(StatusPedido.PEDIDO_EM_ENTREGA);
        pedidoService.setEstadoDoPedido(new PedidoEmEntrega());

    }
}
