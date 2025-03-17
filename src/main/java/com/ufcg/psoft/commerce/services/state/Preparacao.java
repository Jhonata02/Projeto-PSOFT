package com.ufcg.psoft.commerce.services.state;

import com.ufcg.psoft.commerce.model.Enums.StatusPedido;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.services.Impl.PedidoServiceImpl;

public class Preparacao implements StateStatusDoPedido{

    @Override
    public void alterarStatus(Pedido pedido, PedidoServiceImpl pedidoService) {
        if(pedido.getStatusPedido() != StatusPedido.PREPARACAO) {
            throw new RuntimeException("O Status do pedido não é PREPARAÇÃO");
        }
        pedido.setStatusPedido(StatusPedido.PEDIDO_PRONTO);
        pedidoService.setEstadoDoPedido(new PedidoPronto());

    }
}
